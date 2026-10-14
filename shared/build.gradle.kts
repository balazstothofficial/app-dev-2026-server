import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * This common module contains code that is shared between the Android app and the server.
 * It provides a central place for logic and data models that should be reused across modules.
 * Although this version uses the JVM plugin, it could also be created as a Kotlin Multiplatform
 * or Kotlin Native module, allowing platforms that cannot depend on JVM modules to use the same
 * shared code.
 *
 * Resources
 * - [Kotlin multiplatform](https://kotlinlang.org/docs/multiplatform.html)
 * - [Kotlin serialization](https://kotlinlang.org/docs/serialization.html)
 */

// Kotlin JVM plugin for compiling shared Kotlin code targeting the JVM
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

// Java compatibility settings for the shared module
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// Kotlin compiler settings matching the Java version
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

// Shared dependencies used by both client and server modules
dependencies {
    implementation(libs.kotlinx.serialization.json)
}
