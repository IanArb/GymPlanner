package com.ianarbuckle.gymplanner.android.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ianarbuckle.gymplanner.android.ui.common.LoadingButton
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun LoginScreen(
    innerPaddingValues: PaddingValues,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit = {},
    isUsernameValid: Boolean = true,
    isPasswordValid: Boolean = true,
    onUsernameInvalid: (Boolean) -> Unit = {},
    onPasswordInvalid: (Boolean) -> Unit = {},
    isLoading: Boolean = false,
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
            .background(Color.White),
    ) {
        SubcomposeAsyncImage(
            model = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.9p7bkBavk1wVth4ccX5IzwAAAA%26pid%3DApi&f=1&ipt=0fa5a6e9280c12867c00226f63e19134154691845c2e55da100a516f9b9e33ce&ipo=images",
            contentDescription = "Backdrop",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop,
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.error),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Error loading image")
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(top = 350.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    onUsernameChange(it)
                    onUsernameInvalid(username.isNotEmpty())
                },
                label = { Text("Username") },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isUsernameValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Username is required",
                    color = MaterialTheme.colorScheme.error,
                    modifier = modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    onPasswordChange(it)
                    onPasswordInvalid(password.isNotEmpty())
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
            if (!isPasswordValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Password is required",
                    color = MaterialTheme.colorScheme.error,
                    modifier = modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = onRememberMeChange
                )
                Text("Remember Me")
            }
            LoadingButton(
                text = if (isLoading) "" else "Login",
                isLoading = isLoading,
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreenPreview() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isUsernameValid by rememberSaveable { mutableStateOf(false) }
    var isPasswordValid by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(true) }


    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Login") }
                )
            }
        ) { innerPadding ->
            LoginScreen(
                innerPaddingValues = innerPadding,
                username = username,
                onUsernameChange = { username = it },
                password = password,
                onPasswordChange = { password = it },
                onLoginClick = {},
                isUsernameValid = isUsernameValid,
                isPasswordValid = isPasswordValid,
                onUsernameInvalid = { isUsernameValid = it },
                onPasswordInvalid = { isPasswordValid = it },
                rememberMe = rememberMe,
                onRememberMeChange = { rememberMe = it }
            )
        }
    }
}