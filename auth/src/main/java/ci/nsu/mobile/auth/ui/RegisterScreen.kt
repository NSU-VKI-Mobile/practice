package ci.nsu.mobile.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.GroupDto
import ci.nsu.mobile.domain.model.PersonDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    val genders = listOf("MALE", "FEMALE")
    var expandedGender by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(genders[0]) }

    var expandedGroup by remember { mutableStateOf(false) }

    // 🟢 Читаем группы напрямую из ViewModel (без заглушек)
    val groups by viewModel.groups.collectAsState()

    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    val state by viewModel.registerState.collectAsState()

    // 🟢 ЗАГРУЖАЕМ ГРУППЫ С СЕРВЕРА ПРИ ОТКРЫТИИ ЭКРАНА
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.resetStates()
            onBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Личные данные", style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (ГГГГ-ММ-ДД)") }, modifier = Modifier.fillMaxWidth())

        // Dropdown Пола
        ExposedDropdownMenuBox(
            expanded = expandedGender,
            onExpandedChange = { expandedGender = !expandedGender }
        ) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false }
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                            expandedGender = false
                        }
                    )
                }
            }
        }

        // 🟢 Dropdown Группы (теперь берет данные из server)
        ExposedDropdownMenuBox(
            expanded = expandedGroup,
            onExpandedChange = { if (groups.isNotEmpty()) expandedGroup = !expandedGroup }
        ) {
            OutlinedTextField(
                value = selectedGroup?.groupName ?: "Выберите группу",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedGroup,
                onDismissRequest = { expandedGroup = false }
            ) {
                groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.groupName) },
                        onClick = {
                            selectedGroup = group
                            expandedGroup = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedGroup != null && birthDate.isNotBlank()) {
                    val person = PersonDto(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = if (middleName.isBlank()) null else middleName,
                        birthDate = birthDate,
                        gender = selectedGender,
                        groupId = selectedGroup!!.groupId
                    )
                    viewModel.register(login, password, email, person)
                }
            },
            enabled = state !is AuthState.Loading &&
                    login.isNotBlank() &&
                    password.isNotBlank() &&
                    selectedGroup != null &&
                    birthDate.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }

        if (selectedGroup == null) {
            Text("⚠️ Выберите группу выше", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        TextButton(onClick = onBackToLogin) { Text("Уже есть аккаунт? Войти") }

        if (state is AuthState.Error) {
            Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}