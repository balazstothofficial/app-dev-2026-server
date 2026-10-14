/**
 * This module hosts the backend of the project using the Ktor framework.
 * It shares data models with the app module through the shared module.
 * The Kotlin JVM plugin is used because this is a standard JVM server.
 * The Ktor plugin adds tasks and defaults for running and packaging a Ktor application.
 * The Kotlin Serialization plugin enables encoding and decoding data formats such as JSON.
 *
 * Resources
 * - [Ktor](https://ktor.io/docs/welcome.html)
 * - [Ktor server](https://ktor.io/docs/server-create-a-new-project.html)
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

/**
 * Configure the entry point of the server application.
 * EngineMain starts a Ktor server using the Netty engine.
 *
 * Netty is an asynchronous network engine used by Ktor to run the server.
 * It manages low level networking tasks such as handling connections, reading requests,
 * and sending responses efficiently.
 * Ktor can run on several engines, and Netty is one of the most commonly used for JVM based
 * servers.
 *
 * Resources
 * - [Ktor server engines](https://ktor.io/docs/server-engines.html)
 */
application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    // Shared data models used by both client and server
    implementation(project(":shared"))

    // Core Ktor server libraries and Netty engine
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty)

    // Support for loading server configuration from YAML files
    implementation(libs.ktor.server.config.yaml)

    // Content negotiation for formats such as JSON
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)

    // Logback for structured logging on the server
    implementation(libs.logback.classic)

    // Test dependencies for Kotlin and Ktor server testing
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.ktor.server.test.host)
}

kotlin {
    // Use Java 21 toolchain for compiling Kotlin
    jvmToolchain(21)

    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

// Set Java compatibility for the server module
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.test {
    useJUnitPlatform()
}
