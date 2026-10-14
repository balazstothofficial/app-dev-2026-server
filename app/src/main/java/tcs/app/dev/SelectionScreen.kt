package tcs.app.dev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tcs.app.dev.shared.OptionId

@Composable
fun SelectionScreen(
    title: String,
    options: List<UIOption>,
    modifier: Modifier = Modifier,
    onSelected: (OptionId) -> Unit = {}
) {
    var selectionId: OptionId? by rememberSaveable(stateSaver = Saver<OptionId?, String>(
        save = { it?.value }, restore = ::OptionId
    )) { mutableStateOf<OptionId?>(null) }
    val selection = options.firstOrNull { it.option.id == selectionId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Outlined.CheckBox,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    title,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                Button(
                    onClick = { selection?.let { onSelected(it.option.id) } },
                    colors = buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor =
                            MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
                        disabledContentColor =
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    ),
                    enabled = selection != null
                ) {
                    Text(stringResource(R.string.next))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(options, key = { it.option.id.value }) { option ->
                RadioRow(
                    image = { modifier ->
                        LoadingBitmapImage(
                            state = option.image,
                            contentDescription = null,
                            modifier = modifier
                        )
                    },
                    title = option.option.title,
                    description = option.option.description,
                    selected = selectionId == option.option.id,
                    onSelected = { selectionId = option.option.id }
                )
            }
        }
    }
}
