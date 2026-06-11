package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.data.dto.PersonDto
import ci.nsu.moble.main.data.dto.RegisterRequest
import ci.nsu.moble.main.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val groups by authViewModel.groups.collectAsState()
    val registerState by authViewModel.registerState.collectAsState()

    // Загружаем группы при старте экрана
    LaunchedEffect(Unit) {
        authViewModel.loadGroups()
    }


    LaunchedEffect(registerState) {
        if (registerState is AuthViewModel.RegisterUiState.Success) {
            onRegisterSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Пол: ")
                RadioButton(
                    selected = gender == "MALE",
                    onClick = { gender = "MALE" }
                )
                Text("Мужской")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = gender == "FEMALE",
                    onClick = { gender = "FEMALE" }
                )
                Text("Женский")
            }
        }

        item {
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = groups.find { it.id == selectedGroupId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
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
        item {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    if (selectedGroupId != null) {
                        val person = PersonDto(
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName.ifEmpty { null },
                            birthDate = birthDate,
                            gender = gender,
                            groupId = selectedGroupId!!
                        )
                        val request = RegisterRequest(
                            login = login,
                            password = password,
                            email = email,
                            phoneNumber = phoneNumber,
                            person = person
                        )
                        authViewModel.register(request)
                    }
                },
                enabled = registerState !is AuthViewModel.RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (registerState is AuthViewModel.RegisterUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Зарегистрироваться")
                }
            }
        }
        if (registerState is AuthViewModel.RegisterUiState.Error) {
            item {
                Text(
                    text = (registerState as AuthViewModel.RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}