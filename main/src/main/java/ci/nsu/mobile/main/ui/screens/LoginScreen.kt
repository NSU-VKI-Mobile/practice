package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    openRegister: () -> Unit,
    openHome: () -> Unit
) {

    var login by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier =
            Modifier.padding(16.dp)
    ) {

        OutlinedTextField(
            value = login,
            onValueChange = {
                login = it
            },
            label = { Text("Логин") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Пароль") }
        )

        Button(
            onClick = {
                vm.login(
                    login,
                    password,
                    openHome
                )
            }
        ) {
            Text("Войти")
        }

        TextButton(
            onClick = openRegister
        ) {
            Text("Нет аккаунта?")
        }
    }
}