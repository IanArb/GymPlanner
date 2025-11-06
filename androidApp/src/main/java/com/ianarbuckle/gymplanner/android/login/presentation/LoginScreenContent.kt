package com.ianarbuckle.gymplanner.android.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ianarbuckle.gymplanner.android.ui.common.LoadingButton
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Suppress("LongParameterList", "LongMethod", "MaxLineLength")
@Composable
fun LoginScreenContent(
    innerPaddingValues: PaddingValues,
    username: String,
    password: String,
    rememberMe: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    isUsernameValid: Boolean = true,
    isPasswordValid: Boolean = true,
    isLoading: Boolean = false,
    showError: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier =
            modifier
                .padding(innerPaddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
    ) {
        SubcomposeAsyncImage(
            model =
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Flookaside.fbsbx.com%2Flookaside%2Fcrawler%2Fmedia%2F%3Fmedia_id%3D100064453849645&f=1&nofb=1&ipt=956c582251b3095306494eab9d93e7c34c7629841134fa12b97bf1af1a82ca44",
            contentDescription = "Backdrop",
            modifier = Modifier.fillMaxWidth().height(350.dp),
            contentScale = ContentScale.Crop,
            error = {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Error loading image")
                }
            },
        )
        Column(
            modifier =
                Modifier.padding(top = 350.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                label = { Text("Username") },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions =
                    KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            )
            if (!isUsernameValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Username is required",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            )
            if (!isPasswordValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Password is required",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }

            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Error logging in. Please try again.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = onRememberMeChange)
                Text("Remember Me")
            }
            LoadingButton(
                text = if (isLoading) "" else "Login",
                isLoading = isLoading,
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun LoginScreenPreview() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isUsernameValid by rememberSaveable { mutableStateOf(false) }
    var isPasswordValid by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(true) }

    GymAppTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("Login") }) }) { innerPadding ->
            LoginScreenContent(
                innerPaddingValues = innerPadding,
                username = username,
                onUsernameChange = { username = it },
                password = password,
                onPasswordChange = { password = it },
                onLoginClick = {},
                isUsernameValid = isUsernameValid,
                isPasswordValid = isPasswordValid,
                rememberMe = rememberMe,
                showError = true,
                onRememberMeChange = { rememberMe = it },
            )
        }
    }
}
