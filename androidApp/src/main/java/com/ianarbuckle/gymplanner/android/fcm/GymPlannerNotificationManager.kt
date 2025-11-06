package com.ianarbuckle.gymplanner.android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ianarbuckle.gymplanner.android.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GymPlannerNotificationManager
@Inject
constructor(@ApplicationContext private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "FCM Notifications",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title: String, message: String) {
        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_bubble)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        Log.i(
            "NotificationManager",
            "Showing notification with title: $title and message: $message",
        )

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "fcm_channel"
        const val NOTIFICATION_ID = 1
    }
}
