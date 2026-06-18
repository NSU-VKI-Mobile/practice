package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.Result
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    // Используем rememberSaveable для простых полей, чтобы они сохранялись при повороте,
    // но не вызывали TransactionTooLargeException, так как это простые строки.
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    // Состояние выпадающего меню
    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    val groupsState by viewModel.groupsState.collectAsState()
    val registerState by viewModel.registerState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") })
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") })
        TextField(value = login, onValueChange = { login = it }, label = { Text("Логин") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        // Выбор группы
        when (groupsState) {
            is Result.Loading -> CircularProgressIndicator()
            is Result.Success -> {
                val groups = (groupsState as Result.Success).data

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = if (selectedGroupId != null) {
                            groups.find { it.groupId == selectedGroupId }?.groupName ?: ""
                        } else {
                            "Выберите группу"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Группа") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        groups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group.groupName) },
                                onClick = {
                                    selectedGroupId = group.groupId
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            is Result.Error -> Text("Ошибка загрузки групп", color = MaterialTheme.colorScheme.error)
            Result.Idle -> TODO()
        }

        Button(
            onClick = {
                val gid = selectedGroupId ?: 1 // Дефолтное значение, если вдруг null
                val person = PersonDto(firstName, lastName, "", "2000-01-01", "Male", gid)
                val req = RegisterRequest(login, password, email, null, 1, true, person)
                viewModel.register(req)
            },
            enabled = registerState !is Result.Loading && selectedGroupId != null
        ) {
            Text("Зарегистрироваться")
        }

        if (registerState is Result.Success) {
            Text("Успешно! Перейдите ко входу.", color = MaterialTheme.colorScheme.primary)
            // Очищаем поля после успеха, чтобы не сохранять их в состоянии
            firstName = ""
            lastName = ""
            login = ""
            password = ""
            email = ""
            selectedGroupId = null

            LaunchedEffect(Unit) {
                // Небольшая задержка, чтобы пользователь увидел сообщение
                kotlinx.coroutines.delay(1000)
                onNavigateBack()
            }
        }

        if (registerState is Result.Error) {
            Text((registerState as Result.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}