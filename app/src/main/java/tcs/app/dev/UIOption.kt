package tcs.app.dev

import tcs.app.dev.shared.Option

// The shared API model stays independent of Android and screen loading state.
data class UIOption(
    val option: Option,
    val image: ImageState = ImageState.Loading
)
