package ci.nsu.mobile.main.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val groupsState by viewModel.groupsState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Поля формы
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var selectedGroupId by remember { mutableStateOf(0) }
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Обработка ошибок
    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            snackbarHostState.showSnackbar((uiState as UiState.Error).message)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Регистрация", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                // Имя
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Имя *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Фамилия
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Фамилия *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Отчество
                OutlinedTextField(
                    value = middleName,
                    onValueChange = { middleName = it },
                    label = { Text("Отчество") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Дата рождения
                // Объявляем состояние для ошибки даты (перед TextField)
                var dateError by remember { mutableStateOf<String?>(null) }

                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { newValue: String ->  // <-- Явно указываем тип String
                        dateOfBirth = newValue
                        // Простая валидация формата DD.MM.YYYY
                        val dateRegex = Regex("^\\d{2}\\.\\d{2}\\.\\d{4}$")
                        dateError = if (newValue.isNotEmpty() && !dateRegex.matches(newValue)) {
                            "Формат: ДД.ММ.ГГГГ"
                        } else {
                            null
                        }
                    },
                    label = { Text("Дата рождения (ДД.ММ.ГГГГ) *") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("31.12.2000") },
                    isError = dateError != null,
                    supportingText = dateError?.let { { Text(it) } },
                    singleLine = true
                )

                // Пол
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (gender == "MALE") "Мужской" else "Женский",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Пол *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Мужской") },
                            onClick = {
                                gender = "MALE"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Женский") },
                            onClick = {
                                gender = "FEMALE"
                                expanded = false
                            }
                        )
                    }
                }

                // Группа (выпадающий список)
                when (val state = groupsState) {
                    is UiState.Idle -> {}
                    is UiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is UiState.Success -> {
                        var groupExpanded by remember { mutableStateOf(false) }
                        val groups = state.data
                        val selectedGroup = groups.find { it.id == selectedGroupId }

                        ExposedDropdownMenuBox(
                            expanded = groupExpanded,
                            onExpandedChange = { groupExpanded = !groupExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedGroup?.name ?: "Выберите группу *",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Группа *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) }
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
                    is UiState.Error -> {
                        Text(text = "Ошибка загрузки групп: ${state.message}", color = MaterialTheme.colorScheme.error)
                    }
                }

                // Логин
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Логин *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Пароль
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль *") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Телефон
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Телефон *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Кнопка регистрации
                Button(
                    onClick = {
                        val person = PersonDto(
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName.ifBlank { null },
                            birthDate = dateOfBirth,
                            gender = gender,
                            groupId = selectedGroupId
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
                        viewModel.register(request)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = firstName.isNotBlank() && lastName.isNotBlank() &&
                            login.isNotBlank() && password.isNotBlank() &&
                            email.isNotBlank() && phoneNumber.isNotBlank() &&
                            dateOfBirth.isNotBlank() && selectedGroupId != 0
                ) {
                    Text("Зарегистрироваться")
                }

                // Кнопка назад
                TextButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Уже есть аккаунт? Войти")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Индикатор загрузки
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // DatePicker Dialog
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val datePickerState = rememberDatePickerState()

        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Выберите дату рождения") },
            text = {
                androidx.compose.material3.DatePicker(
                    state = rememberDatePickerState(
                        initialSelectedDateMillis = calendar.timeInMillis
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateOfBirth = sdf.format(Date(selectedDate))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}