package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.ui.viewmodel.AuthState
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    // Основные поля
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Поля PersonDto
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") } // Формат YYYY-MM-DD

    // Пол
    val genders = listOf("MALE", "FEMALE")
    var expandedGender by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(genders[0]) }

    // Группы
    var expandedGroup by remember { mutableStateOf(false) }

    // 🟢 Читаем группы напрямую из ViewModel
    val groups by viewModel.groups.collectAsState()

    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    val state by viewModel.registerState.collectAsState()

    // Загружаем группы при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    // Обновляем список групп из ViewModel (если добавите StateFlow для групп)
    // Пока используем простой вызов, но лучше сделать через StateFlow

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.resetStates()
            onBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Добавляем скролл, так как полей много
            .padding(24.dp),
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

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("2000-01-01") }
        )

        // Выбор пола
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

        // Выбор группы
        // Примечание: В реальном коде группы должны приходить из ViewModel.
        // Здесь я использую заглушку списка, если groups пуст, чтобы UI не ломался
        val displayGroups = if (groups.isEmpty()) listOf(GroupDto(1, "Загрузка...")) else groups

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
                displayGroups.forEach { group ->
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
            enabled = state !is AuthState.Loading && login.isNotBlank() && selectedGroup != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }

        TextButton(onClick = onBackToLogin) { Text("Уже есть аккаунт? Войти") }

        if (state is AuthState.Error) {
            Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}