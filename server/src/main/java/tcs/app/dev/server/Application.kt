package tcs.app.dev.server

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

/**
 * # Entry point for the server module.
 *
 * The main function launches the Ktor server by delegating to [EngineMain], which reads the
 * server configuration from application.yaml and starts the Netty engine.
 *
 * The [Application.module] function is the module referenced in the Ktor configuration file.
 * Ktor automatically calls this function during startup. It is used to install features,
 * such as routing and serialization, by delegating to helper functions.
 *
 * ## Resources:
 * - [Ktor](https://ktor.io/docs/welcome.html)
 * - [Ktor modules](https://ktor.io/docs/server-modules.html)
 * - [Ktor engines](https://ktor.io/docs/server-engines.html)
 */
fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
}
