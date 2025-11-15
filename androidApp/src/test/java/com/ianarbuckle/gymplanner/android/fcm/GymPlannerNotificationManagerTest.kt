package com.ianarbuckle.gymplanner.android.fcm

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GymPlannerNotificationManagerTest {

    private lateinit var gymPlannerNotificationManager: GymPlannerNotificationManager

    private val context = mockk<Context>(relaxed = true)
    private val notificationManager = mockk<NotificationManager>(relaxed = true)

    private val mockNotification = mockk<Notification>(relaxed = true)

    @Before
    fun setUp() {
        gymPlannerNotificationManager = GymPlannerNotificationManager(notificationManager)

        mockkConstructor(NotificationCompat.Builder::class)

        every { anyConstructed<NotificationCompat.Builder>().build() } returns mockNotification
    }

    @Test
    fun `showNotification should call notify on notificationManager`() {
        val title = "Test Title"
        val message = "Test Message"

        gymPlannerNotificationManager.showNotification(context, title, message)

        verify { notificationManager.notify(any(), any()) }
    }

    @Test
    fun `create channel should call createNotificationChannel on notificationManager`() {
        gymPlannerNotificationManager.createNotificationChannel()

        verify { notificationManager.createNotificationChannel(any()) }
    }
}
