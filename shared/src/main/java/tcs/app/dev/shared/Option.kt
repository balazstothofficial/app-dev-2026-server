package tcs.app.dev.shared

import kotlinx.serialization.Serializable

/**
 * Shared data model used by both the Android app and the server.
 * Keeping this class in the shared module ensures that all platforms use the same structure.
 * The Serializable annotation allows this model to be encoded and decoded not only as JSON,
 * but also in other supported formats such as ProtoBuf or CBOR.
 *
 * Resources:
 * - [Kotlin serialization](https://kotlinlang.org/docs/serialization.html)
 */
@Serializable
data class Option(
    val id: OptionId,
    val title: String,
    val description: String
)
