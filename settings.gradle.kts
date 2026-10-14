/**
 * Resources
 * - [Gradle plugins](https://docs.gradle.org/current/userguide/plugins.html)
 * - [Repositories](https://docs.gradle.org/current/userguide/declaring_repositories.html)
 * - [Multi-module builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
 */

// Configure where Gradle should look for plugins used in this project
pluginManagement {
    repositories {

        // Google repository for Android and Jetpack related plugins
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        // Repository for many Java and Kotlin libraries and plugins
        mavenCentral()

        // Default Gradle plugin repository
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // Do not allow modules to define their own repositories
    // All modules must use the repositories defined below
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        // Google repository for Android and Jetpack libraries
        google()

        // Central repository for most open source libraries
        mavenCentral()
    }
}

// Name of the entire project
rootProject.name = "Server"

// Modules included in this Gradle project
include(":app") // Android application module
include(":server") // Backend server module
include(":shared") // Shared Kotlin module used by both app and server
