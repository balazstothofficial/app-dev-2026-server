package tcs.app.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcs.app.dev.data.getOptionImage
import tcs.app.dev.data.getOptions
import tcs.app.dev.data.postSelection
import tcs.app.dev.shared.OptionId

/**
 * Uses the same screen-owned image loading as the mock-API exercise.
 * This example reuses each image in the list and details, and sends selections to the server.
 * Sending a selection is a user action, so it runs in viewModelScope independently of the screen.
 */
class SelectionViewModel : ViewModel() {

    data class State(
        val options: List<UIOption> = emptyList(),
        val selectionId: OptionId? = null,
        val errorMessage: String? = null
    ) {
        val selection: UIOption? get() = options.firstOrNull { it.option.id == selectionId }
    }

    private val mutableState = MutableStateFlow(State())
    val state = mutableState.asStateFlow()

    suspend fun loadOptions() = coroutineScope {
        mutableState.update { it.copy(errorMessage = null) }
        val options = getOptions()
        if (options.isFailure) {
            mutableState.update { it.copy(errorMessage = options.exceptionOrNull()?.message) }
            return@coroutineScope
        }

        val uiOptions = options.getOrThrow().map(::UIOption)
        mutableState.update { it.copy(options = uiOptions) }

        uiOptions.forEach { option ->
            launch {
                val image = loadImage(option.option.id)
                mutableState.update { state ->
                    state.copy(options = state.options.map {
                        if (it.option.id == option.option.id) it.copy(image = image) else it
                    })
                }
            }
        }
    }

    private suspend fun loadImage(id: OptionId): ImageState = withContext(Dispatchers.IO) {
        repeat(4) { attempt ->
            getOptionImage(id).getOrNull()?.let { return@withContext ImageState.Loaded(it) }
            if (attempt < 3) delay(250)
        }
        ImageState.Failed
    }

    fun select(selection: OptionId?) {
        if (state.value.selectionId == selection) return
        val option = state.value.options.firstOrNull { it.option.id == selection }?.option
        mutableState.update { state ->
            state.copy(selectionId = selection)
        }

        if (option != null) {
            viewModelScope.launch {
                val response = postSelection(option)

                val errorMessage = response.exceptionOrNull()?.message
                if (errorMessage != null) {
                    mutableState.update { state -> state.copy(errorMessage = errorMessage) }
                }
            }
        }
    }
}
