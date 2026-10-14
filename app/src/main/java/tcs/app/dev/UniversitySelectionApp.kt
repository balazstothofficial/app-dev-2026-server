package tcs.app.dev

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Like the mock-API example, LaunchedEffect owns option and image loading.
 * The server supplies one image per university, reused in both the list and details.
 * A Snackbar displays network errors, while each image has Loading, Loaded, or Failed state.
 *
 * ## Resources
 * - [Snackbar](https://developer.android.com/develop/ui/compose/components/snackbar)
 */
@Composable
fun UniversitySelectionApp(
    modifier: Modifier = Modifier,
    viewModel: SelectionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel) {
        viewModel.loadOptions()
    }

    LaunchedEffect(state.errorMessage) {
        val errorMessage = state.errorMessage
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Box(Modifier.fillMaxSize()) {
        when (val selection = state.selection) {
            null -> {
                SelectionScreen(
                    title = stringResource(R.string.university_selection),
                    options = state.options,
                    modifier = modifier
                ) { selected -> viewModel.select(selected) }
            }

            else -> {
                BackHandler {
                    viewModel.select(null)
                }
                DetailsScreen(
                    title = selection.option.title,
                    description = selection.option.description,
                    image = { modifier ->
                        LoadingBitmapImage(
                            state = selection.image,
                            contentDescription = null,
                            modifier = modifier
                        )
                    },
                    modifier = modifier
                )
            }
        }

        SnackbarHost(
            snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
