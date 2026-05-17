plugins { id("com.android.application") }

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

android {
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") { isMinifyEnabled = false }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}
