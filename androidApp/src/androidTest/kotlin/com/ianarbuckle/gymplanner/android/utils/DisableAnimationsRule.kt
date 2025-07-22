package com.ianarbuckle.gymplanner.android.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DisableAnimationsRule : TestWatcher() {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context
    private val uiDevice: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    override fun starting(description: Description) {
        disableAnimations()
    }

    override fun finished(description: Description) {
        enableAnimations()
    }

    private fun disableAnimations() {
        if (isAnimationSupportGranted()) {
            uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
            uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
            uiDevice.executeShellCommand("settings put global window_animation_scale 0")
        }
    }

    private fun enableAnimations() {
        if (isAnimationSupportGranted()) {
            uiDevice.executeShellCommand("settings put global animator_duration_scale 1")
            uiDevice.executeShellCommand("settings put global transition_animation_scale 1")
            uiDevice.executeShellCommand("settings put global window_animation_scale 1")
        }
    }

    private fun isAnimationSupportGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkCallingOrSelfPermission("android.permission.CHANGE_CONFIGURATION") ==
                PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
