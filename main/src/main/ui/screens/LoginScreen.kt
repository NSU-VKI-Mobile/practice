package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.ui.components.LoadingDialog
import ci.nsu.mobile.main.ui.components.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.loginState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccess()
            onLoginSuccess()
        }
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        ErrorDialog(
            message = state.error ?: "Ошибка",
            onDismiss = { /* Ошибка исчезнет при следующем действии */ }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Вход в систему",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = state.login,
                onValueChange = { viewModel.onLoginChange(it) },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.loginError != null,
                supportingText = {
                    if (state.loginError != null) {
                        Text(state.loginError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.passwordError != null,
                supportingText = {
                    if (state.passwordError != null) {
                        Text(state.passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (showPassword) PasswordVisualTransformation() else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Войти")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Нет аккаунта? Зарегистрироваться")
            }
        }
    }
}