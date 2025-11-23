package com.ianarbuckle.gymplanner.android.chat.di

import com.ianarbuckle.gymplanner.android.chat.fakes.FakeChatRepository
import com.ianarbuckle.gymplanner.android.chat.fakes.FakeMessagesRepository
import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [ChatModule::class])
@Module
class FakeChatModule {

    @Singleton
    @Provides
    fun providesChatRepository(): ChatRepository {
        return FakeChatRepository()
    }

    @Singleton
    @Provides
    fun providesMessageRepository(): MessagesRepository {
        return FakeMessagesRepository()
    }
}
