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

// Auto-enable the dev-server image proxy only when running a development task.
// Production builds emit an empty path so the rewrite is a no-op and original
// image URLs are used (a real backend proxy is required for prod).
val isDevBuild: Boolean =
    gradle.startParameter.taskNames.any { it.contains("Development", ignoreCase = true) }
val imageProxyPath: String =
    project.findProperty("gymplanner.web.imageProxyPath")?.toString()
        ?: if (isDevBuild) "/img-proxy" else ""
val ddgProxyPath: String =
    project.findProperty("gymplanner.web.ddgProxyPath")?.toString()
        ?: if (isDevBuild) "/ddg-proxy" else ""

val generateWebBuildConfig by
    tasks.registering {
        val outputDir = layout.buildDirectory.dir("generated/buildConfig/wasmJsMain/kotlin")
        inputs.property("baseUrl", webBaseUrl)
        inputs.property("imageProxyPath", imageProxyPath)
        inputs.property("ddgProxyPath", ddgProxyPath)
        outputs.dir(outputDir)
        doLast {
            val dir = outputDir.get().asFile
            dir.mkdirs()
            dir.resolve("BuildConfig.kt")
                .writeText(
                    """
            package com.ianarbuckle.gymplanner.web

            internal object BuildConfig {
                const val BASE_URL = "$webBaseUrl"
                const val IMAGE_PROXY_PATH = "$imageProxyPath"
                const val DDG_PROXY_PATH = "$ddgProxyPath"
            }
            """
                        .trimIndent()
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
        val wasmJsMain by getting { kotlin.srcDir(generateWebBuildConfig) }
        commonMain {
            dependencies {
                implementation(projects.shared)

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3.jetbrains)
                implementation(libs.compose.components.resources)
                implementation(libs.kotlinx.immutable.collections)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kermit)
                implementation(libs.coil3.compose)
                implementation(libs.coil3.network.ktor3)
                implementation(libs.ktor.client.core)
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
