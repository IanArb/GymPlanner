plugins { id("io.gitlab.arturbosch.detekt") }

detekt {
    toolVersion = "1.23.7"
    config.setFrom(files("$rootDir/config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
}
