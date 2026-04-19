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

val webBaseUrl: String =
    project.findProperty("gymplanner.web.baseUrl")?.toString() ?: "http://localhost:8080"

val generateWebBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/buildConfig/wasmJsMain/kotlin")
    inputs.property("baseUrl", webBaseUrl)
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile
        dir.mkdirs()
        dir.resolve("BuildConfig.kt").writeText(
            """
            package com.ianarbuckle.gymplanner.web

            internal object BuildConfig {
                const val BASE_URL = "$webBaseUrl"
            }
            """.trimIndent()
        )
    }
}

kotlin {
    compilerOptions { freeCompilerArgs.add("-Xexplicit-backing-fields") }

    wasmJs {
        browser { commonWebpackConfig { outputFileName = "gymplanner.js" } }
        binaries.executable()
    }

    sourceSets {
        val wasmJsMain by getting {
            kotlin.srcDir(generateWebBuildConfig)
        }
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
