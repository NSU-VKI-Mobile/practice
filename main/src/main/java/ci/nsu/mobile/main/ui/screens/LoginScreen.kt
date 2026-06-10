package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    openRegister: () -> Unit,
    openHome: () -> Unit
) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Авторизация", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !loading
        )

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                vm.login(login, password, openHome)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading && login.isNotEmpty() && password.isNotEmpty()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Войти")
            }
        }

        TextButton(
            onClick = openRegister,
            enabled = !loading
        ) {
            Text("Нет аккаунта? Регистрация")
        }
    }
}