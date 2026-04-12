package com.ianarbuckle.gymplanner.web.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.web.generated.resources.Res
import com.ianarbuckle.gymplanner.web.generated.resources.ic_chevron_right
import com.ianarbuckle.gymplanner.web.ui.theme.Black
import com.ianarbuckle.gymplanner.web.ui.theme.OffWhite
import org.jetbrains.compose.resources.painterResource

enum class GymLocation(val displayName: String) {
    CLONTARF("Clontarf"),
    WESTMANSTOWN("Westmanstown"),
    ASTON_QUAY("Aston Quay"),
    LEOPARDSTOWN("Leopardstown"),
    SANDYMOUNT("Sandymount"),
    DUN_LAOGHAIRE("Dún Laoghaire"),
}

@Composable
fun LoginScreen(
    onSignInClick: (username: String, password: String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    authError: String? = null,
) {
    val validator = remember { FieldValidator() }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberWorkstation by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf(GymLocation.CLONTARF) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize().background(OffWhite)) {
        Text(
            text = "Westwood",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            letterSpacing = 1.5.sp,
            color = Color(0xFF0D0D0D),
            modifier = Modifier.align(Alignment.TopStart).padding(24.dp),
        )

        LoginCard(
            username = username,
            password = password,
            rememberWorkstation = rememberWorkstation,
            selectedLocation = selectedLocation,
            usernameError = usernameError,
            passwordError = passwordError,
            authError = authError,
            isLoading = isLoading,
            onUsernameChange = {
                username = it
                usernameError = null
            },
            onPasswordChange = {
                password = it
                passwordError = null
            },
            onRememberChange = { rememberWorkstation = it },
            onLocationSelected = { selectedLocation = it },
            onSignInClick = {
                val isUsernameValid = validator.validateUsername(username)
                val isPasswordValid = validator.validatePassword(password)
                usernameError = if (!isUsernameValid) "Please enter a valid username" else null
                passwordError =
                    if (!isPasswordValid) "Password must be at least 6 characters" else null
                if (isUsernameValid && isPasswordValid) {
                    onSignInClick(username, password)
                }
            },
            onForgotPasswordClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun LoginCard(
    username: String,
    password: String,
    rememberWorkstation: Boolean,
    selectedLocation: GymLocation,
    usernameError: String?,
    passwordError: String?,
    authError: String?,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onLocationSelected: (GymLocation) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(380.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            AuthorizedAccessLabel()

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MANAGER ACCESS",
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                color = Color(0xFF0D0D0D),
            )

            Spacer(modifier = Modifier.height(32.dp))

            FieldLabel(text = "GYM LOCATION")
            Spacer(modifier = Modifier.height(8.dp))
            LocationDropdown(
                selectedLocation = selectedLocation,
                onLocationSelected = onLocationSelected,
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "USERNAME")
            UnderlineTextField(
                value = username,
                onValueChange = onUsernameChange,
                placeholder = "manager_username",
                errorMessage = usernameError,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FieldLabel(text = "PASSWORD")
                TextButton(onClick = onForgotPasswordClick, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "FORGOT PASSWORD?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        letterSpacing = 0.5.sp,
                    )
                }
            }
            UnderlineTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "",
                isPassword = true,
                errorMessage = passwordError,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberWorkstation,
                    onCheckedChange = onRememberChange,
                    colors =
                        CheckboxDefaults.colors(
                            uncheckedColor = Color(0xFFBDBDBD),
                            checkedColor = Black,
                        ),
                )
                Text(
                    text = "Remember this workstation",
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authError != null) {
                Text(
                    text = authError,
                    color = Color(0xFFB00020),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            SignInButton(onClick = onSignInClick, isLoading = isLoading)
        }
    }
}

@Composable
private fun LocationDropdown(
    selectedLocation: GymLocation,
    onLocationSelected: (GymLocation) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedLocation.displayName,
                fontSize = 14.sp,
                color = Black,
                fontWeight = FontWeight.Medium,
            )
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(16.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(316.dp).background(Color.White),
        ) {
            GymLocation.entries.forEach { location ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = location.displayName,
                            fontSize = 14.sp,
                            fontWeight =
                                if (location == selectedLocation) FontWeight.SemiBold
                                else FontWeight.Normal,
                            color = Black,
                        )
                    },
                    onClick = {
                        onLocationSelected(location)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(textColor = Black),
                )
            }
        }
    }
}

@Composable
private fun AuthorizedAccessLabel() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(24.dp).height(2.dp).background(Black))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "AUTHORIZED ACCESS",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF9E9E9E),
            letterSpacing = 1.sp,
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF9E9E9E),
        letterSpacing = 1.sp,
    )
}

@Composable
private fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    errorMessage: String? = null,
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, fontSize = 14.sp, color = Color(0xFFBDBDBD)) },
            visualTransformation =
                if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            isError = errorMessage != null,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Black,
                    unfocusedIndicatorColor = Color(0xFFE0E0E0),
                    cursorColor = Black,
                    focusedTextColor = Color(0xFF0D0D0D),
                    unfocusedTextColor = Color(0xFF0D0D0D),
                ),
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFB00020),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            )
        }
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isLoading) Color(0xFF616161) else Black)
                .clickable(enabled = !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            Text(
                text = "SIGNING IN…",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "SIGN IN",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "→",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
