plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.realm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Ktor

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.serialization.kotlinx.json)
            implementation(libs.ktor.client.cio)

            // Kotlinx
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            // Realm
            implementation(libs.realm.base)

            // Datetime
            implementation(libs.kotlinx.datetime)

            // Koin
            api(libs.koin.core)

            // Datastore
            api(libs.androidx.datastore.preferences)
            api(libs.androidx.datastore.preferences.core)

            implementation(libs.kotlinx.immutable.collections)

            implementation(libs.kermit)
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
                implementation(libs.kotest.assertions.core)
            }
        }

        androidMain.dependencies {
            implementation("com.squareup.okhttp3:okhttp:4.9.3")
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.ianarbuckle.gymplanner"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.48.2").userData(mapOf("android" to "true"))
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("0.48.2")
    }
}

detekt {
    toolVersion = "1.23.7"
    config = files("$rootDir/config/detekt.yml")
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
}
