package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
    var gender by remember { mutableStateOf("MALE") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Состояния для выпадающих меню
    var groupExpanded by remember { mutableStateOf(false) }

    val registerState by authViewModel.registerState.collectAsState()
    val groupsState by groupViewModel.groupsState.collectAsState()

    // Загрузка групп
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок
        Text(
            text = "Registration",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Имя
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        // Фамилия
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        // Отчество
        OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Middle Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Дата рождения
        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Birth Date (YYYY-MM-DD) *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = { Text("2000-01-01") }
        )

        // Выбор пола (RadioButton)
        Text("Gender *", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "MALE",
                    onClick = { gender = "MALE" }
                )
                Text("Male", modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "FEMALE",
                    onClick = { gender = "FEMALE" }
                )
                Text("Female", modifier = Modifier.padding(start = 4.dp))
            }
        }

        // Выбор группы
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
                    // Используем groupId и groupName вместо id и name
                    val selectedGroup = groups.find { it.groupId == selectedGroupId }
                    TextField(
                        value = selectedGroup?.groupName ?: "",
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
                                text = { Text(group.groupName) },  // groupName вместо name
                                onClick = {
                                    selectedGroupId = group.groupId  // groupId вместо id
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
                        text = "Failed to load groups",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Логин
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login *") },
            modifier = Modifier.fillMaxWidth()
        )

        // Пароль
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password *") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Телефон
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        // Кнопка регистрации
        Button(
            onClick = {
                if (validateForm(firstName, lastName, login, password, email, birthDate, selectedGroupId)) {
                    val person = PersonDto(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = selectedGroupId!!
                    )

                    val request = RegisterRequest(
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        roleId = 1,
                        authAllowed = true,
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

        // Кнопка назад
        TextButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }

        // Ошибки
        when (val state = registerState) {
            is AuthState.Error -> {
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
            else -> {}
        }
    }
}

private fun validateForm(
    firstName: String,
    lastName: String,
    login: String,
    password: String,
    email: String,
    birthDate: String,
    groupId: Int?
): Boolean {
    return firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            login.isNotBlank() &&
            password.isNotBlank() &&
            email.isNotBlank() &&
            birthDate.isNotBlank() &&
            groupId != null
}