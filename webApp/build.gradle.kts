@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {

    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "gymplanner.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared)

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3.jetbrains)
                implementation(libs.compose.components)
            }
        }
    }

}