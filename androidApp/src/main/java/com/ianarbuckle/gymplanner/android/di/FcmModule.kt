package com.ianarbuckle.gymplanner.android.di

import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.ianarbuckle.gymplanner.fcm.FcmTokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FcmModule {

    @Provides
    fun providesFcmTokenRepository(): FcmTokenRepository {
        return FcmTokenRepository()
    }

    @Provides
    fun providesFirebaseMessaging(): FirebaseMessaging {
        return Firebase.messaging
    }
}
