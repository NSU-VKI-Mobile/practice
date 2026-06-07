package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.auth.viewModels.login.LoginEvents
import ci.nsu.mobile.auth.viewModels.login.LoginViewModel
import ci.nsu.mobile.domain.navigation.Screens
import ci.nsu.mobile.ui.components.CustomButton
import ci.nsu.mobile.ui.components.TextFieldWithOptionalStar

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit,
                navTo: (String) -> Unit,
                viewModel: LoginViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.loginEvent(LoginEvents.CleanAll)
            onLoginSuccess()
        }
    }
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text("Вход", fontSize = 45.sp)
            Spacer(Modifier.height(60.dp))
            TextFieldWithOptionalStar(
                value = state.login,
                onValueChange = {
                    viewModel.loginEvent(LoginEvents.LoginChanged(it))
                },
                hasStar = false,
                placeholder = "Логин",
                isError = "Login" in state.errorFields
            )
            TextFieldWithOptionalStar(
                value = state.password,
                onValueChange = {
                    viewModel.loginEvent(LoginEvents.PasswordChanged(it))
                },
                hasStar = false,
                placeholder = "Пароль",
                trailingIcon = {
                    val icon = if (state.passwordState)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff
                    val contentDescription = if (state.passwordState) "Показать пароль" else "Скрыть пароль"
                    IconButton({ viewModel.loginEvent(LoginEvents.PasswordVisibilityChanged(!state.passwordState))}) {
                        Icon(icon, contentDescription)
                    }
                },
                visualTransformation = if (!state.passwordState) PasswordVisualTransformation() else VisualTransformation.None,
                isError = "Password" in state.errorFields
            )
            CustomButton({
                viewModel.loginEvent(LoginEvents.SubmitLogin)
            }, "Войти")
            Spacer(Modifier.padding((20.dp)))

            Text(
                buildAnnotatedString {
                    append("Нет аккаунта? ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Зарегистрироваться")
                    }
                },
                modifier = Modifier.clickable { navTo(Screens.RegistrationScreen.route) },
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp))
                Text("Вход...")
            }
            if (state.errorMessage != null) {
                Text(state.errorMessage.toString(), color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}