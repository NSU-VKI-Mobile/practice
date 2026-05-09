package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    onNavToReg: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Подписываемся на единое состояние экрана
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Вход в систему",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = uiState.login, // Берем из UI State
                        onValueChange = { viewModel.onLoginChanged(it) }, // Сообщаем VM об изменениях
                        label = { Text("Логин") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    OutlinedTextField(
                        value = uiState.pass, // Берем из UI State
                        onValueChange = { viewModel.onPasswordChanged(it) }, // Сообщаем VM об изменениях
                        label = { Text("Пароль") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    }

                    uiState.error?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { viewModel.login(onLoginSuccess) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading && uiState.login.isNotBlank() && uiState.pass.isNotBlank()
                    ) {
                        Text("Войти")
                    }

                    TextButton(onClick = onNavToReg, enabled = !uiState.isLoading) {
                        Text("Нет аккаунта? Зарегистрироваться")
                    }
                }
            }
        }
    }
}