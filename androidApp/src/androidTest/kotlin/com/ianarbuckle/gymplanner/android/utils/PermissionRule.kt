package com.ianarbuckle.gymplanner.android.utils

import android.os.Build
import androidx.test.rule.GrantPermissionRule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A custom [TestRule] that applies a [GrantPermissionRule] only if the device's SDK version meets
 * the specified minimum level.
 *
 * @param permission The name of the permission to grant (e.g.,
 *   android.Manifest.permission.POST_NOTIFICATIONS).
 * @param minSdk The minimum SDK level on which to grant the permission.
 */
class ConditionalPermissionRule(private val permission: String, private val minSdk: Int) :
    TestRule {

    override fun apply(base: Statement?, description: Description?): Statement? {
        if (Build.VERSION.SDK_INT < minSdk) {
            return base
        }

        return GrantPermissionRule.grant(permission).apply(base, description)
    }
}
