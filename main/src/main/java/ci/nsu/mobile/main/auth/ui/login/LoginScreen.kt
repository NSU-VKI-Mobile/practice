package ci.nsu.mobile.main.auth.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.auth.ui.components.ProgressBar

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState.loginState is LoginState.Loading

    // Handle navigation on success
    LaunchedEffect(uiState.loginState) {
        when (uiState.loginState) {
            is LoginState.Success -> {
                onLoginSuccess()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Вход",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.login,
                onValueChange = viewModel::updateLogin,
                label = { Text("Логин") },
                isError = uiState.loginError != null,
                supportingText = {
                    uiState.loginError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти")
            }

            TextButton(
                onClick = onNavigateToRegister,
                enabled = !isLoading
            ) {
                Text("Нет аккаунта? Зарегистрироваться")
            }

            if (uiState.loginState is LoginState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (uiState.loginState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        if (isLoading) {
            ProgressBar()
        }
    }
}