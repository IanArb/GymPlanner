package com.ianarbuckle.gymplanner.android.chat.di

import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.DefaultChatRepository
import com.ianarbuckle.gymplanner.chat.DefaultMessagesRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ChatModule {

  @Provides
  fun providesChatRepository(): ChatRepository {
    return DefaultChatRepository()
  }

  @Provides
  fun providesMessageRepository(): MessagesRepository {
    return DefaultMessagesRepository()
  }
}
