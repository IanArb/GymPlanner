package com.ianarbuckle.gymplanner.android.chat.di

import com.ianarbuckle.gymplanner.android.chat.fakes.FakeChatRepository
import com.ianarbuckle.gymplanner.android.chat.fakes.FakeMessagesRepository
import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [ChatModule::class])
@Module
class FakeChatModule {

    @Provides
    fun providesChatRepository(): ChatRepository {
        return FakeChatRepository()
    }

    @Provides
    fun providesMessageRepository(): MessagesRepository {
        return FakeMessagesRepository()
    }
}
