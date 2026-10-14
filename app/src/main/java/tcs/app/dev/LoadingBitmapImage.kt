package tcs.app.dev

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource

@Composable
fun LoadingBitmapImage(
    state: ImageState,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AnimatedContent(state) { imageState ->
        when (imageState) {
            ImageState.Loading -> CircularProgressIndicator(
                modifier = modifier,
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            is ImageState.Loaded -> Image(
                bitmap = imageState.bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = modifier
            )
            ImageState.Failed -> Text(
                text = stringResource(R.string.image_unavailable),
                modifier = modifier
            )
        }
    }
}
