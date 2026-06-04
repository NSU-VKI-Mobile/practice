package ci.nsu.mobile.main.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: AuthViewModel = viewModel()

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val error by viewModel.error.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Вход", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                viewModel.login(login, password) {

                    navController.navigate("main") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        Spacer(Modifier.height(10.dp))

        TextButton(onClick = {
            navController.navigate("register")
        }) {
            Text("Нет аккаунта? Регистрация")
        }

        error?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}