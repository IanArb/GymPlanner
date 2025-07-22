package gymplanner.chat

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.chat.ChatScreenViewModel
import com.ianarbuckle.gymplanner.android.chat.ChatUiState
import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import gymplanner.utils.TestCoroutineRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTests {

  @get:Rule val testCoroutineRule = TestCoroutineRule()

  private val chatRepository: ChatRepository = mockk()
  private val messagesRepository: MessagesRepository = mockk()
  private val savedStateHandle: SavedStateHandle = mockk()
  private val viewModel: ChatScreenViewModel =
    ChatScreenViewModel(chatRepository, messagesRepository, savedStateHandle)

  @Test
  fun `test getAllMessages success updates state to MessagesSuccess`() = runTest {
    every { savedStateHandle.get<String>("userId") } returns "testUserId"
    every { savedStateHandle.get<String>("username") } returns "TestUser"

    val messages =
      persistentListOf(
        Message(
          username = "User1",
          text = "Hello, this is a test message!",
          formattedTime = "2025-10-01 12:34:56",
        ),
        Message(username = "User2", text = "Hi there!", formattedTime = "2025-10-01 12:35:56"),
      )

    coEvery { chatRepository.initSession(any(), any()) } returns Result.success(Unit)
    coEvery { messagesRepository.getMessages() } returns Result.success(messages)
    coEvery { chatRepository.observeMessages() } returns flowOf()

    viewModel.connect()

    viewModel.chatUiState.test {
      assert(awaitItem() is ChatUiState.Idle)
      assert(awaitItem() is ChatUiState.Loading)
      val success = awaitItem() as ChatUiState.MessagesSuccess
      assert(success.messages == messages)
    }
  }

  @Test
  fun `test getAllMessages success updates state to Failed`() = runTest {
    every { savedStateHandle.get<String>("userId") } returns "testUserId"
    every { savedStateHandle.get<String>("username") } returns "TestUser"

    coEvery { chatRepository.initSession(any(), any()) } returns Result.success(Unit)
    coEvery { messagesRepository.getMessages() } returns Result.failure(IOException())
    coEvery { chatRepository.observeMessages() } returns flowOf()

    viewModel.connect()

    viewModel.chatUiState.test {
      assert(awaitItem() is ChatUiState.Idle)
      assert(awaitItem() is ChatUiState.Loading)
      assert(awaitItem() is ChatUiState.Failed)
    }
  }

  @Test
  fun `test state returns failed when initSession() failed`() = runTest {
    every { savedStateHandle.get<String>("userId") } returns "testUserId"
    every { savedStateHandle.get<String>("username") } returns "TestUser"

    val messages =
      persistentListOf(
        Message(
          username = "User1",
          text = "Hello, this is a test message!",
          formattedTime = "2025-10-01 12:34:56",
        ),
        Message(username = "User2", text = "Hi there!", formattedTime = "2025-10-01 12:35:56"),
      )

    coEvery { chatRepository.initSession(any(), any()) } returns Result.failure(IOException())
    coEvery { messagesRepository.getMessages() } returns Result.success(messages)
    coEvery { chatRepository.observeMessages() } returns flowOf()

    viewModel.connect()

    viewModel.chatUiState.test {
      assert(awaitItem() is ChatUiState.Idle)
      assert(awaitItem() is ChatUiState.Loading)
      assert(awaitItem() is ChatUiState.MessagesSuccess)
      assert(awaitItem() is ChatUiState.Failed)
    }
  }

  @Test
  fun `test sendMessage updates message text state to empty when successful`() = runTest {
    coEvery { chatRepository.sendMessage(any()) } returns Result.success(Unit)

    viewModel.sendMessage()

    viewModel.messageText.test { assertEquals("", awaitItem()) }
  }

  @Test
  fun `test sendMessage updates message text state to empty when failed`() = runTest {
    coEvery { chatRepository.sendMessage(any()) } returns Result.failure(IOException())

    viewModel.sendMessage()

    viewModel.messageText.test { assertEquals("", awaitItem()) }
  }

  @Test
  fun `test onMessageChanged updates message text state`() = runTest {
    val newMessage = "New message text"
    viewModel.onMessageChanged(newMessage)

    viewModel.messageText.test { assertEquals(newMessage, awaitItem()) }
  }

  @Test
  fun `test disconnect calls closeSession on chatRepository`() = runTest {
    coEvery { chatRepository.closeSession() } just Runs

    viewModel.disconnect()

    advanceUntilIdle()

    coVerify { chatRepository.closeSession() }
  }
}
