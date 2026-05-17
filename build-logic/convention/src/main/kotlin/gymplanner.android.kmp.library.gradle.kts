plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(17)
    android {
        compileSdk = 36
        minSdk = 24
    }
}
