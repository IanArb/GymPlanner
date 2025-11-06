package com.ianarbuckle.gymplanner.android.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ianarbuckle.gymplanner.fcm.FcmTokenRepository
import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DefaultFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var dataStoreRepository: DataStoreRepository
    @Inject lateinit var fcmTokenRepository: FcmTokenRepository
    @Inject lateinit var gymPlannerNotificationManager: GymPlannerNotificationManager

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        gymPlannerNotificationManager.createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        coroutineScope.launch {
            val userId = dataStoreRepository.getStringData(USER_ID) ?: ""
            if (userId.isNotEmpty()) {
                fcmTokenRepository
                    .registerToken(FcmTokenRequest(userId = userId, token = token))
                    .fold(
                        onSuccess = {
                            Log.i("DefaultFirebaseMsgService", "FCM Token registered successfully")
                        },
                        onFailure = {
                            Log.e("DefaultFirebaseMsgService", "Error registering FCM token: $it")
                        },
                    )
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            gymPlannerNotificationManager.showNotification(it.title ?: "", it.body ?: "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
