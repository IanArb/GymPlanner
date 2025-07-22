package gymplanner.utils

import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule
import java.io.File

internal fun createRoborazziRule(): RoborazziRule {
    return RoborazziRule(
        options =
            RoborazziRule.Options(
                outputDirectoryPath = "screenshots",
                outputFileProvider = { description, outputDirectory, fileExtension ->
                    File(
                        outputDirectory,
                        "${description.className}/${description.methodName}.$fileExtension",
                    )
                },
                roborazziOptions = DefaultRoborazziOptions,
            )
    )
}

private val DefaultRoborazziOptions: RoborazziOptions =
    RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.6f),
        recordOptions = RoborazziOptions.RecordOptions(resizeScale = 1.0),
    )
