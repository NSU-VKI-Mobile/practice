package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    // Поля формы
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedGroupId by remember { mutableStateOf(0) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    // Загружаем группы при старте
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    // Обработка успешной регистрации
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccessState()
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Регистрация", style = MaterialTheme.typography.headlineMedium) }

        item { OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Отчество (необязательно)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Пол (MALE/FEMALE)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item {
            var expanded by remember { mutableStateOf(false) }
            val groups = uiState.availableGroups
            val selectedGroup = groups.find { it.id == selectedGroupId }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedGroup?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    enabled = !uiState.isLoading
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupId = group.id
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        item { OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item { OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) }

        item {
            Button(
                onClick = {
                    viewModel.register(
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName.ifEmpty { null },
                        birthDate = birthDate,
                        gender = gender,
                        groupId = selectedGroupId
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Зарегистрироваться")
                }
            }
        }

        item { uiState.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        } }
    }
}