package com.ianarbuckle.gymplanner.android.chat.di

import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.DefaultChatRepository
import com.ianarbuckle.gymplanner.chat.DefaultMessagesRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Module
@InstallIn(ViewModelComponent::class)
class ChatModule {

    @Provides
    fun providesChatRepository(): ChatRepository {
        return DefaultChatRepository()
    }

    @OptIn(ExperimentalTime::class)
    @Provides
    fun providesMessageRepository(): MessagesRepository {
        return DefaultMessagesRepository(Clock.System)
    }
}
