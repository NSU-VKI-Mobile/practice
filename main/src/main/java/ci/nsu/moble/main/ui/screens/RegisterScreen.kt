// Task_6: Экран регистрации — поля: фамилия, имя, отчество, дата рождения, пол, группа, телефон, логин, пароль, email.
package ci.nsu.moble.main.ui.screens

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: MainViewModel) {
    val state by viewModel.registerState.collectAsStateWithLifecycle()
    var groupExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Регистрация", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = { viewModel.onRegisterFieldChanged(lastName = it) },
            label = { Text("Фамилия") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.firstName,
            onValueChange = { viewModel.onRegisterFieldChanged(firstName = it) },
            label = { Text("Имя") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.patronymic,
            onValueChange = { viewModel.onRegisterFieldChanged(patronymic = it) },
            label = { Text("Отчество") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.birthDate,
            onValueChange = { viewModel.onRegisterFieldChanged(birthDate = it) },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.gender,
            onValueChange = { viewModel.onRegisterFieldChanged(gender = it) },
            label = { Text("Пол") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = groupExpanded,
            onExpandedChange = { groupExpanded = !groupExpanded }
        ) {
            OutlinedTextField(
                value = state.groups.find { it.id == state.selectedGroupId }?.name
                    ?: "Выберите группу",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(groupExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                enabled = !state.loading
            )
            ExposedDropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = { groupExpanded = false }
            ) {
                state.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            viewModel.onGroupSelected(group.id)
                            groupExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = { viewModel.onRegisterFieldChanged(phoneNumber = it) },
            label = { Text("Телефон") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.login,
            onValueChange = { viewModel.onRegisterFieldChanged(login = it) },
            label = { Text("Логин") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onRegisterFieldChanged(password = it) },
            label = { Text("Пароль") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onRegisterFieldChanged(email = it) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        )

        val errorText = state.error
        if (errorText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorText, color = androidx.compose.ui.graphics.Color.Red, fontSize = 14.sp)
        }

        if (state.success) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Регистрация прошла успешно!",
                color = androidx.compose.ui.graphics.Color(0xFF4CAF50), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    strokeWidth = 2.dp,
                    color = androidx.compose.ui.graphics.Color.White
                )
            } else {
                Text("Зарегистрироваться", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { viewModel.navigateTo(Screen.Login) }) {
            Text("Уже есть аккаунт? Войти", fontSize = 14.sp)
        }
    }
}
