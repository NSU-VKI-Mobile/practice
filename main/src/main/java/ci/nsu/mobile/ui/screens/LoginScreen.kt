package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(vm: AuthViewModel, navController: NavController) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(vm.isLoggedIn) {
        if (vm.isLoggedIn) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Авторизация", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(login, { login = it }, label = { Text("Логин") })

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(password, { password = it }, label = { Text("Пароль") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = { vm.login(login, password) }) {
            Text("Войти")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Нет аккаунта? Регистрация")
        }

        vm.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        if (vm.isLoading) CircularProgressIndicator()
    }
}