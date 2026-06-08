package ci.nsu.mobile.main.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.components.DatePickerField
import ci.nsu.mobile.main.ui.components.Dropdown

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegister: () -> Unit,
    onToLogin: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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

        DatePickerField(
            date = viewModel.birthDate,
            onDateSelected = {
                viewModel.birthDate = it
            },
            modifier = Modifier.fillMaxWidth()
        )



        Dropdown (
            label = "Пол",
            items = viewModel.genders,
            selected = viewModel.selectedGender,
            onSelect = {
                viewModel.selectedGender = it
            },
            itemLabel = { it }
        )



        Dropdown (
            label = "Группа",
            items = viewModel.groups,
            selected = viewModel.selectedGroup,
            onSelect = {
                viewModel.selectedGroup = it
            },
            itemLabel = { it.name }
        )


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
            onClick = onRegister,
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
            onClick = onToLogin
        ) {
            Text("Назад")
        }
    }
}