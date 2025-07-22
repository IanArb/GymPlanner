package com.ianarbuckle.gymplanner.android.chat.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun Composer(
  onMessageChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  isEnabled: Boolean,
  modifier: Modifier = Modifier,
  composerText: String = "",
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  Column(modifier.padding(8.dp)) {
    OutlinedTextField(
      value = composerText,
      onValueChange = onMessageChange,
      shape = RoundedCornerShape(12.dp),
      placeholder = { Text("Type a message") },
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
      keyboardActions =
        KeyboardActions(
          onSend = {
            if (isEnabled) {
              onSendMessage()
              keyboardController?.hide()
            }
          }
        ),
      trailingIcon = {
        if (isEnabled) {
          IconButton(
            onClick = {
              onSendMessage()
              keyboardController?.hide()
            },
            modifier = Modifier.testTag("SendButton"),
          ) {
            Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "Send message")
          }
        } else {
          // Placeholder for the trailing icon when the input is empty
          Icon(
            imageVector = Icons.AutoMirrored.Default.Send,
            contentDescription = "Send message",
            tint =
              androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
          )
        }
      },
    )
  }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ComposerPreview() {
  GymAppTheme {
    Scaffold(
      bottomBar = { Composer(onMessageChange = {}, onSendMessage = {}, isEnabled = true) }
    ) { paddingValues ->
      Column(modifier = Modifier.fillMaxWidth().padding(paddingValues)) {}
    }
  }
}
