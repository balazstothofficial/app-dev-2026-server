/**
 * This file defines plugin versions once for the entire project.
 * The plugins are declared here but not applied, so each module can choose which ones it needs.
 * This avoids duplication, keeps plugin versions consistent, and ensures all modules use the
 *  same definitions from the version catalog.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}
