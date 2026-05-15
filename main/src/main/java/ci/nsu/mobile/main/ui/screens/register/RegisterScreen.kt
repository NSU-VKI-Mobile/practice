package ci.nsu.mobile.main.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository

@Composable
fun RegisterScreen(
    navController: NavController,
    retrofitClient: RetrofitClient,
    tokenManager: TokenManager
) {

    val viewModel: RegisterViewModel = viewModel {
        RegisterViewModel(
            (
                    AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager)
        )
        )
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = {
                viewModel.firstName = it
            },
            label = {
                Text("Имя")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = {
                viewModel.lastName = it
            },
            label = {
                Text("Фамилия")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.middleName,
            onValueChange = {
                viewModel.middleName = it
            },
            label = {
                Text("Отчество")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.birthDate,
            onValueChange = {
                viewModel.birthDate = it
            },
            label = {
                Text("Дата рождения")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.gender,
            onValueChange = {
                viewModel.gender = it
            },
            label = {
                Text("Пол")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                viewModel.selectedGroup?.name
                    ?: "Выберите группу"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            viewModel.groups.forEach { group ->

                DropdownMenuItem(
                    text = {
                        Text(group.name)
                    },
                    onClick = {
                        viewModel.selectedGroup = group
                        expanded = false
                    }
                )
            }
        }

        OutlinedTextField(
            value = viewModel.login,
            onValueChange = {
                viewModel.login = it
            },
            label = {
                Text("Логин")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = {
                viewModel.password = it
            },
            label = {
                Text("Пароль")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = {
                viewModel.phone = it
            },
            label = {
                Text("Телефон")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.register {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }

        viewModel.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        TextButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Назад")
        }
    }
}