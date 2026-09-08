plugins { id("com.diffplug.spotless") }

spotless {
    kotlin {
        target("**/*.kt")
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_property-naming" to "disabled"
            )
        )
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
