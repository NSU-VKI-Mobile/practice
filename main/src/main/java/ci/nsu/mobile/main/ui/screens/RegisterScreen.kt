package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.RegisterViewModel
import ci.nsu.mobile.main.viewmodel.RegisterViewModelFactory
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.network.RetrofitInstance
import ci.nsu.mobile.main.utils.TokenManager
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val apiService = remember { RetrofitInstance.create(tokenManager) }
    val authRepository = remember { AuthRepository(apiService, tokenManager) }
    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(authRepository)
    )

    val uiState by registerViewModel.uiState.collectAsState()
    val groups by registerViewModel.groups.collectAsState()

    // Состояния для выпадающих списков
    var groupExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    // Состояние для календаря
    var showDatePicker by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Список возможных значений пола
    val genderOptions = listOf("MALE", "FEMALE")

    // Форматирование даты для отображения (ДД.ММ.ГГГГ)
    val displayDate = remember(uiState.birthDate) {
        if (uiState.birthDate.isBlank()) {
            ""
        } else {
            try {
                // Ожидаем формат ГГГГ-ММ-ДД
                val parts = uiState.birthDate.split("-")
                if (parts.size == 3) {
                    "${parts[2]}.${parts[1]}.${parts[0]}"  // ДД.ММ.ГГГГ
                } else {
                    uiState.birthDate
                }
            } catch (e: Exception) {
                uiState.birthDate
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ФАМИЛИЯ ==============
        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = { registerViewModel.updateField("lastName", it) },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ИМЯ ==============
        OutlinedTextField(
            value = uiState.firstName,
            onValueChange = { registerViewModel.updateField("firstName", it) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ОТЧЕСТВО ==============
        OutlinedTextField(
            value = uiState.middleName,
            onValueChange = { registerViewModel.updateField("middleName", it) },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ДАТА РОЖДЕНИЯ (С КАЛЕНДАРЕМ) ==============
        OutlinedTextField(
            value = displayDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата рождения") },
            placeholder = { Text("Выберите дату") },
            trailingIcon = {
                IconButton(
                    onClick = { showDatePicker = true },
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Выбрать дату"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ПОЛ (ВЫПАДАЮЩИЙ СПИСОК) ==============
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = it }
        ) {
            OutlinedTextField(
                value = when (uiState.gender) {
                    "MALE" -> "Мужской"
                    "FEMALE" -> "Женский"
                    else -> uiState.gender
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !uiState.isLoading,
                placeholder = { Text("Выберите пол") }
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (gender == "MALE") "Мужской" else "Женский",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            registerViewModel.updateField("gender", gender)
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ГРУППА (ВЫПАДАЮЩИЙ СПИСОК) ==============
        ExposedDropdownMenuBox(
            expanded = groupExpanded,
            onExpandedChange = { groupExpanded = it }
        ) {
            OutlinedTextField(
                value = groups.find { it.id == uiState.selectedGroupId }?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !uiState.isLoading,
                placeholder = { Text("Выберите группу") }
            )
            ExposedDropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = { groupExpanded = false }
            ) {
                groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name, modifier = Modifier.fillMaxWidth()) },
                        onClick = {
                            registerViewModel.selectGroup(group.id)
                            groupExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ЛОГИН ==============
        OutlinedTextField(
            value = uiState.login,
            onValueChange = { registerViewModel.updateField("login", it) },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ПАРОЛЬ ==============
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { registerViewModel.updateField("password", it) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== EMAIL ==============
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { registerViewModel.updateField("email", it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ТЕЛЕФОН ==============
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { registerViewModel.updateField("phoneNumber", it) },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ============== КНОПКА РЕГИСТРАЦИИ ==============
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { registerViewModel.register(onRegisterSuccess) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.selectedGroupId != 0 &&
                        uiState.gender.isNotBlank() &&
                        uiState.birthDate.isNotBlank()
            ) {
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ============== ССЫЛКА НА ВХОД ==============
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Уже есть аккаунт? Войти")
        }

        // ============== ОТОБРАЖЕНИЕ ОШИБКИ ==============
        if (uiState.error != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Ошибка",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ошибка регистрации",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ============== ДИАЛОГ ВЫБОРА ДАТЫ ==============
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = null,
                yearRange = 1900..2026
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                // ПРАВИЛЬНОЕ ФОРМАТИРОВАНИЕ ДАТЫ
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = millis

                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH) + 1  // +1 потому что январь = 0
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                // Форматируем в ГГГГ-ММ-ДД (двузначные месяц и день)
                                val formattedDate = String.format("%04d-%02d-%02d", year, month, day)

                                println("=== ВЫБРАНА ДАТА ===")
                                println("Год: $year, Месяц: $month, День: $day")
                                println("Отформатировано: $formattedDate")

                                registerViewModel.updateField("birthDate", formattedDate)
                            }
                            showDatePicker = false
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text("Выбрать")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Выберите дату рождения",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}