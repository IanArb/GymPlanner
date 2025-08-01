package com.ianarbuckle.gymplanner.android.utils

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.getKoinApplicationOrNull
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.unloadKoinModules
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module

class KoinTestRule(private val modules: List<Module>) : TestWatcher() {
    override fun starting(description: Description) {
        if (getKoinApplicationOrNull() == null) {
            startKoin {
                androidContext(
                    InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                )
                modules(modules)
            }
        } else {
            loadKoinModules(modules)
        }
    }

    override fun finished(description: Description) {
        unloadKoinModules(modules)
    }
}
