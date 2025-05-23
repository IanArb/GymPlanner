plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.roborazzi).apply(false)
    alias(libs.plugins.spotless).apply(false)
    alias(libs.plugins.detekt).apply(false)
    alias(libs.plugins.google.services.gms).apply(false)
}
