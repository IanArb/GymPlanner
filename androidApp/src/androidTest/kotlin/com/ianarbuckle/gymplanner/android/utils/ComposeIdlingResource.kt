package com.ianarbuckle.gymplanner.android.utils

import androidx.test.espresso.IdlingResource

class ComposeIdlingResource : IdlingResource {
  @Volatile private var isIdle = true
  private var resourceCallback: IdlingResource.ResourceCallback? = null

  override fun getName(): String {
    return this::class.java.name
  }

  override fun isIdleNow(): Boolean {
    return isIdle
  }

  override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
    resourceCallback = callback
  }

  fun setIdleState(isIdleNow: Boolean) {
    isIdle = isIdleNow
    if (isIdleNow && resourceCallback != null) {
      resourceCallback?.onTransitionToIdle()
    }
  }
}
