plugins {
    id("gymplanner.android.application")
    id("gymplanner.spotless")
    id("gymplanner.detekt")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.screenshot)
    alias(libs.plugins.google.services.gms)
    alias(libs.plugins.stability.analyzer)
}

kotlin {
    compilerOptions { freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode") }
}

android {
    namespace = "com.ianarbuckle.gymplanner.android"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
    defaultConfig {
        applicationId = "com.ianarbuckle.gymplanner.android"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.ianarbuckle.gymplanner.android.utils.CustomTestRunner"

        buildConfigField("String", "BASE_URL", "\"https://c6ee5300188f.ngrok-free.app\"")
        buildConfigField("String", "WEBSOCKET_URL", "\"wss://775372c5564a.ngrok-free.app\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        getByName("release") { resValue("string", "clear_text_config", "false") }
        getByName("debug") { resValue("string", "clear_text_config", "true") }
    }
}

dependencies {
    constraints {
        implementation("androidx.concurrent:concurrent-futures:1.2.0") {
            because("Hilt testing 2.60.1 requires it in the instrumented-test runtime")
        }
    }

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

    implementation(libs.coil3.compose)
    implementation(libs.coil3.network.okhttp)

    implementation(libs.kotlinx.datetime)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)

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
    androidTestImplementation(libs.coil3.test)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(kotlin("test"))

    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.compose.ui.tooling)
    screenshotTestImplementation(libs.kotlinx.immutable.collections)
    screenshotTestImplementation(libs.kotlinx.datetime)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

tasks.withType<Test> { useJUnit() }
