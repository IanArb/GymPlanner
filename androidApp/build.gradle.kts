plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services.gms)
    alias(libs.plugins.stability.analyzer)
}

kotlin { jvmToolchain(17) }

android {
    namespace = "com.ianarbuckle.gymplanner.android"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.ianarbuckle.gymplanner.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.ianarbuckle.gymplanner.android.utils.CustomTestRunner"

        buildConfigField("String", "BASE_URL", "\"https://0de380bab7ed.ngrok-free.app\"")
        buildConfigField("String", "WEBSOCKET_URL", "\"wss://775372c5564a.ngrok-free.app\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes +=
                "META-INF/LICENSE-notice.md" // Add this line to exclude the conflicting file
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "clear_text_config", "false")
        }
        getByName("debug") {
            isMinifyEnabled = false
            resValue("string", "clear_text_config", "true")
        }
    }

    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(projects.shared)

    detektPlugins(libs.detekt.compose)

    platform(libs.compose.bom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.viewmodel)

    implementation(libs.coil)

    implementation(libs.kotlinx.datetime)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.tracing)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.messaging)

    implementation(libs.ktor.client.android)

    implementation(libs.koin.android)

    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.espresso.idling.resource)

    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.koin.android)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.test.uiautomator)

    testImplementation(libs.compose.ui.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.junit.android.ext)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
    testImplementation(libs.roboelectric)
    testImplementation(libs.koin.test)
    testImplementation(kotlin("test"))
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

roborazzi {
    // Directory for reference images
    outputDir.set(file("src/screenshots"))
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

detekt {
    toolVersion = "1.23.7"
    config = files("$rootDir/config/detekt.yml")
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
}

tasks.withType<Test> {
    // Disable tests for release build for Roborazzi compatibility
    if (name == "testReleaseUnitTest") {
        enabled = false
    }
    useJUnit()
}
