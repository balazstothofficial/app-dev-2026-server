package tcs.app.dev.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import tcs.app.dev.shared.Option
import tcs.app.dev.shared.OptionId

/**
 * # Contains all network calls made by the Android app to the Ktor server.
 *
 * - The [baseUrl] points to `10.0.2.2`, which is the special Android emulator address that maps
 *   to localhost on the development machine. This allows the app to access the local Ktor
 *   server running on port `8080`.
 *
 * - Each function returns network errors as a Result, but lets cancellation propagate to the caller.
 *
 * ## Resources
 * - [Ktor client request](https://ktor.io/docs/client-requests.html)
 * - [Android emulator networking](https://developer.android.com/studio/run/emulator-networking)
 */
const val baseUrl = "http://10.0.2.2:8080"

/**
 * Sends a GET request to /options and deserializes the response into a list of Option.
 */
suspend fun getOptions(): Result<List<Option>> = apiResult {
    client.get("$baseUrl/options").body()
}

/**
 * Requests the binary image data for a given option ID and converts the raw bytes into
 * an Android Bitmap.
 */
suspend fun getOptionImage(id: OptionId): Result<Bitmap> = apiResult {
    val bytes: ByteArray = client.get("$baseUrl/options/${id.value}/image") {
        accept(ContentType.Image.Any)
    }.body()

    checkNotNull(BitmapFactory.decodeByteArray(bytes, 0, bytes.size)) { "Invalid image response" }
}

/**
 * Sends a POST request with the selected Option as JSON. If the server responds with a
 * non-successful status code, an exception is thrown with either the response message
 * or the status description.
 */
suspend fun postSelection(option: Option): Result<Unit> = apiResult {
    val response = client.post("$baseUrl/selection") {
        contentType(ContentType.Application.Json)
        setBody(option)
    }

    if (!response.status.isSuccess()) {
        val message = apiResult { response.body<String>() }.getOrNull()

        // Better would be a custom exception and message
        throw Exception(if (message.isNullOrBlank()) response.status.description else message)
    }
}

// Unlike runCatching, cancellation is not converted into a network error.
private inline fun <T> apiResult(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(e)
}
