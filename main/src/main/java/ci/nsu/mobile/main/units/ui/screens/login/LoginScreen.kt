package ci.nsu.mobile.main.units.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(viewModel.loginSucces) {
        if (viewModel.loginSucces) onLoginSuccess()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)){
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = viewModel.login, onValueChange = {viewModel.login = it}, label = { Text("Логин") })
        OutlinedTextField(value = viewModel.password, onValueChange = {viewModel.password = it}, label = {Text("Пароль")}, visualTransformation = PasswordVisualTransformation())
        if(viewModel.isLoading) LinearProgressIndicator()
        Button(onClick = { viewModel.performLogin() }) { Text("Войти")}
        TextButton(onClick = onNavigateToRegister) { Text("Нет аккаунта? Зарегистрироваться")}
        viewModel.errorMessage?.let {Text(it, color = MaterialTheme.colorScheme.error)}
    }
}