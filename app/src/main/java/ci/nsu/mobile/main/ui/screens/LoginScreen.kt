package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.viewmodel.AuthState
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit
) {
    var login by remember { mutableStateOf("") } // 🟢 Переименовали username → login
    var password by remember { mutableStateOf("") }
    val state by viewModel.loginState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) viewModel.resetStates()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") }, // 🟢 Подпись "Логин"
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(login, password) }, // 🟢 Передаём login, password
            enabled = state !is AuthState.Loading && login.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Войти")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToRegister) { Text("Нет аккаунта? Зарегистрироваться") }

        if (state is AuthState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}