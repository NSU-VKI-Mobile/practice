package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.viewmodel.AuthState
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.GroupListState
import ci.nsu.mobile.main.viewmodel.GroupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    groupViewModel: GroupViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Поля формы
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Состояния для выпадающих меню
    var genderExpanded by remember { mutableStateOf(false) }
    var groupExpanded by remember { mutableStateOf(false) }

    // Варианты для выбора
    val genderOptions = listOf("MALE", "FEMALE")

    // Состояния из ViewModel
    val registerState by authViewModel.registerState.collectAsState()
    val groupsState by groupViewModel.groupsState.collectAsState()

    // Загрузка групп при открытии экрана
    LaunchedEffect(Unit) {
        groupViewModel.loadGroups()
    }

    // Обработка успешной регистрации
    LaunchedEffect(registerState) {
        if (registerState is AuthState.Success) {
            onRegisterSuccess()
            authViewModel.resetStates()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок
        item {
            Text(
                text = "Registration",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Фамилия
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name *") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Имя
        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name *") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Отчество
        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Middle Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Дата рождения
        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Birth Date (YYYY-MM-DD) *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                placeholder = { Text("2000-01-01") }
            )
        }

        // Выбор пола (Gender)
        item {
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                TextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Выбор группы
        item {
            when (groupsState) {
                is GroupListState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is GroupListState.Success -> {
                    val groups = (groupsState as GroupListState.Success).groups
                    ExposedDropdownMenuBox(
                        expanded = groupExpanded,
                        onExpandedChange = { groupExpanded = it }
                    ) {
                        val selectedGroup = groups.find { it.id == selectedGroupId }
                        TextField(
                            value = selectedGroup?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Group *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = groupExpanded,
                            onDismissRequest = { groupExpanded = false }
                        ) {
                            groups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group.name) },
                                    onClick = {
                                        selectedGroupId = group.id
                                        groupExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                is GroupListState.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Failed to load groups: ${(groupsState as GroupListState.Error).message}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Логин
        item {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login *") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Пароль
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password *") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        // Email
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

        // Телефон
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        }

        // Кнопка регистрации
        item {
            Button(
                onClick = {
                    if (validateForm(firstName, lastName, login, password, email, birthDate, gender, selectedGroupId)) {
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
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is AuthState.Loading
            ) {
                if (registerState is AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Register")
                }
            }
        }

        // Кнопка возврата к логину
        item {
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Login")
            }
        }

        // Отображение ошибок
        when (val state = registerState) {
            is AuthState.Error -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

// Функция валидации формы
private fun validateForm(
    firstName: String,
    lastName: String,
    login: String,
    password: String,
    email: String,
    birthDate: String,
    gender: String,
    groupId: Int?
): Boolean {
    return firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            login.isNotBlank() &&
            password.isNotBlank() &&
            email.isNotBlank() &&
            birthDate.isNotBlank() &&
            gender.isNotBlank() &&
            groupId != null
}