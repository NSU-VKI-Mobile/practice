package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.repository.Result
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = login, onValueChange = { login = it }, label = { Text("Логин") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login(login, password) }, enabled = state !is Result.Loading) {
            Text("Войти")
        }

        if (state is Result.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        if (state is Result.Error) {
            Text(text = (state as Result.Error).message, color = MaterialTheme.colorScheme.error)
        }

        LaunchedEffect(state) {
            if (state is Result.Success) {
                onLoginSuccess()
            }
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}