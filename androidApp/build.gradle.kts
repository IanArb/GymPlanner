plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.ianarbuckle.gymplanner.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.ianarbuckle.gymplanner.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.ianarbuckle.gymplanner.android.utils.CustomTestRunner"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md" // Add this line to exclude the conflicting file
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            resValue("string", "clear_text_config","false")
        }
        getByName("debug") {
            isMinifyEnabled = false
            resValue("string", "clear_text_config","true")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(projects.shared)

    platform(libs.compose.bom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.shimmer)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil)

    implementation(libs.kotlinx.datetime)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.kotlinx.immutable.collections)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    compileOnly(libs.realm.base)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)

    ksp(libs.koin.ksp)
    implementation(libs.koin.annotations)

    implementation(libs.androidx.tracing)

    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.espresso.idling.resource)
    // Add the AndroidJUnitRunner dependency
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.koin.android)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.espresso.intents)

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
    testImplementation(libs.koin.core)
}

// Compile time check
ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

roborazzi {
    // Directory for reference images
    outputDir.set(file("src/screenshots"))
}

tasks.withType<Test> {
    useJUnit()
}