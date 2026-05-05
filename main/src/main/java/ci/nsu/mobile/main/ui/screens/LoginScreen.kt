package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.ui.components.CustomButton
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.viewmodel.state.LoginEvents

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit,
                navTo: (String) -> Unit,
                viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.loginEvent(LoginEvents.CleanAll)
            onLoginSuccess()
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField(state.login,
            onValueChange = {
                viewModel.loginEvent(LoginEvents.LoginChanged(it))
            },
            label = {Text("Login")},
            placeholder = {Text("Введите логин")},
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(state.password,
            onValueChange = {
                viewModel.loginEvent(LoginEvents.PasswordChanged(it))
            },
            placeholder = {Text("Введите пароль")},
            label = {Text("Password")},
            modifier = Modifier.padding(bottom = 10.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        CustomButton({
            viewModel.loginEvent(LoginEvents.SubmitLogin)
            }, "Войти")
        Spacer(Modifier.padding((20.dp)))
        Text("Нет аккаунта? Зарегистрироваться",
            modifier = Modifier.clickable(
                onClick =  {navTo(Screens.RegistrationScreen.route)}
            ))
        if (state.errorMessage != null) {
            Text(state.errorMessage.toString(), color = Color.Red)
        }
    }
}