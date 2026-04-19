@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.spotless)
}

compose.resources { packageOfResClass = "com.ianarbuckle.gymplanner.web.generated.resources" }

kotlin {
    compilerOptions { freeCompilerArgs.add("-Xexplicit-backing-fields") }

    wasmJs {
        browser { commonWebpackConfig { outputFileName = "gymplanner.js" } }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared)

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3.jetbrains)
                implementation(libs.compose.components.resources)
                implementation(libs.kotlinx.immutable.collections)
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
            }
        }
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }
}
