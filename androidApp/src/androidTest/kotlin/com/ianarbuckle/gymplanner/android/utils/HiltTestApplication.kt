package com.ianarbuckle.gymplanner.android.utils

import android.app.Application
import com.ianarbuckle.gymplanner.android.BaseApplication
import com.ianarbuckle.gymplanner.di.initKoin
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseApplication::class)
class HiltTestApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    initKoin(
      baseUrl = "http://localhost:8080/",
      websocketBaseUrl = "wss://localhost:8000/",
      dataStore = FakeDataStore(),
    )
  }
}
