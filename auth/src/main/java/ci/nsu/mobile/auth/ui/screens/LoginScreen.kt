package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.auth.ui.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel, onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit, onQrScanClick: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(vm.qrLogin, vm.qrPassword) {
        vm.qrLogin?.let { login = it }
        vm.qrPassword?.let { password = it }
        vm.clearQrData()
    }

    Column(
        Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { vm.login(login, password, onLoginSuccess) }, modifier = Modifier.fillMaxWidth()
        ) { Text("Войти") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onQrScanClick, modifier = Modifier.fillMaxWidth()
        ) { Text("Авторизация через QR-код") }

        TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
            Text("Зарегистрироваться")
        }

        if (vm.isLoading) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}