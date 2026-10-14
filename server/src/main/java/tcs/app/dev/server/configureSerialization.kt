package tcs.app.dev.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

/**
 * # Configures serialization for the server.
 *
 * This function installs the [ContentNegotiation] plugin, which allows the server to automatically
 * convert incoming and outgoing data. The `json()` function enables JSON serialization using
 * `kotlinx.serialization`, making it possible to send and receive typed data models.
 *
 * ## Resources
 * - [Ktor content negotiation](https://ktor.io/docs/serialization-client.html#content-negotiation)
 * - [Serialization](https://kotlinlang.org/docs/serialization.html)
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
