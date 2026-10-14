package tcs.app.dev.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tcs.app.dev.shared.Option
import tcs.app.dev.shared.OptionId

// Stores how often each Option was selected. The server keeps this in memory.
val selections = mutableMapOf<OptionId, UInt>()

// Mutex to ensure thread safe access to the selections map, because multiple requests
// may update it at the same time.
val selectionsMutex = Mutex()

/**
 * # Configures all HTTP routes for the server.
 *
 * This function registers endpoints using Ktor's routing feature.
 * The route shows how to:
 * - send data
 * - receive data
 * - work with URL parameters
 * - return binary image data
 * - perform synchronized updates using a Mutex
 *
 * The routes are basic and not type-safe.
 * With the ktor server resources library we can also make them type safe.
 *
 * ## Resources
 * - [Ktor routing](https://ktor.io/docs/server-routing.html)
 * - [Ktor type-safe routing](https://ktor.io/docs/server-resources.html)
 */
fun Application.configureRouting() {
    routing {
        // Returns the current selection counts for all options.
        // Access is synchronized with a Mutex to avoid race conditions.
        get("/") {
            selectionsMutex.withLock {
                call.respond(selections)
            }
        }

        // Returns a predefined list of Options.
        get("/options") {
            call.respond(MockData.Universities)
        }

        // Returns an image for a given option ID.
        // The ID is read from the URL: /options/{id}/image
        get("/options/{id}/image") {
            val id = call.parameters["id"]?.let(::OptionId)
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            if (MockData.Universities.none { it.id == id }) {
                return@get call.respond(HttpStatusCode.NotFound)
            }

            val stream =
                this::class.java.getResourceAsStream("/images/${id.value}.png")
                    ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respondBytes(
                bytes = stream.use { it.readBytes() },
                contentType = ContentType.Image.PNG
            )
        }

        // Receives a selected Option from the client and updates the counter.
        // For this use-case it would be actually enough to receive the id,
        // but we showcase that it also works for more complex data types.
        // Each request records a new selection event and increments the count. Use POST:
        // repeating it has another effect, whereas PUT must be idempotent.
        post("/selection") {
            val selection = runCatching { call.receive<Option>() }

            if (selection.isSuccess) {
                val selection = selection.getOrThrow()
                if (MockData.Universities.none { it.id == selection.id }) {
                    return@post call.respond(HttpStatusCode.BadRequest)
                }

                selectionsMutex.withLock {
                    val amount = selections[selection.id] ?: 0u
                    selections[selection.id] = amount + 1u
                }
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
