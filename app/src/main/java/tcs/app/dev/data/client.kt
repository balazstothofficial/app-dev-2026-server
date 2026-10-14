package tcs.app.dev.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * # Defines the HTTP client used by the app to communicate with the Ktor server.
 *
 * - This client uses the `OkHttp` engine, which is a widely used networking library on Android.
 *
 * - The `ContentNegotiation` plugin is installed so responses and requests are automatically
 * serialized and deserialized using `kotlinx.serialization` with JSON.
 *
 * - The JSON configuration enables `ignoreUnknownKeys`, which allows the app to handle
 * server responses that may contain additional fields not present in the shared data model.
 *
 * ## Resources
 * - [Ktor client](https://ktor.io/docs/client-create-new-application.html)
 * - [Ktor client engines](https://ktor.io/docs/client-engines.html)
 * - [OkHttp](https://square.github.io/okhttp/)
 * - [Ktor client content negotiation](https://ktor.io/docs/client-serialization.html)
 * - [Serialization](https://kotlinlang.org/docs/serialization.html)
 */
val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}
