import org.jetbrains.kotlin.utils.addToStdlib.butIf

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
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
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
}

dependencies {
    implementation(projects.shared)

    platform(libs.compose.bom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil)

    implementation(libs.kotlinx.datetime)
    debugImplementation(libs.compose.ui.tooling)

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
}

// Compile time check
ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}