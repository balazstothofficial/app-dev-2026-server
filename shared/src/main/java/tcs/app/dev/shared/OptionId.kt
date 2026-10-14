package tcs.app.dev.shared

import kotlinx.serialization.Serializable

/** Shared identity; JSON encodes this value class as a string. */
@Serializable
@JvmInline
value class OptionId(val value: String)
