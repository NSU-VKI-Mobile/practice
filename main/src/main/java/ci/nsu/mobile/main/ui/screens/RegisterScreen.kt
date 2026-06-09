package ci.nsu.mobile.main.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.RegisterViewModel
import ci.nsu.mobile.main.ui.viewmodel.isASCII
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    // Поля формы
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("MALE") }
    var genderExpanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf(0) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val fieldErrors = uiState.fieldErrors

    // DatePicker
    val datePickerState = rememberDatePickerState()
    val selectedDateMillis = datePickerState.selectedDateMillis
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    LaunchedEffect(selectedDateMillis) {
        selectedDateMillis?.let {
            birthDate = dateFormat.format(Date(it))
            viewModel.clearFieldError("birthDate")
        }
    }

    // Фильтры ввода (только для ограничения символов)
    fun filterNameInput(input: String): String {
        return input.filter { it.isLetter() || it == ' ' || it == '-' }
    }

    fun filterLatinLettersAndDigits(input: String): String {
        return input.filter { it.isLetterOrDigit() && it.isASCII() && it != ' ' }
    }

    fun filterPhoneInput(input: String): String {
        // Оставляем только цифры
        val digitsOnly = input.filter { it.isDigit() }

        return when {
            digitsOnly.isEmpty() -> ""
            digitsOnly.length == 1 -> {
                // Первая цифра — обязательно 8
                if (digitsOnly.first() == '8') "8" else ""
            }
            else -> {
                // Проверяем, что первая цифра 8, и обрезаем до 11 цифр
                val firstChar = digitsOnly.first()
                if (firstChar != '8') return ""

                val limited = digitsOnly.take(11)  // максимум 11 цифр (8 + 10 цифр)
                limited
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.loadGroups() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccessState()
            navController.navigate("login") { popUpTo("register") { inclusive = true } }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Регистрация", style = MaterialTheme.typography.headlineMedium) }

        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = filterNameInput(it)
                    viewModel.clearFieldError("firstName")
                },
                label = { Text("Имя") },
                isError = fieldErrors.firstName != null,
                supportingText = { fieldErrors.firstName?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
        }

        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = filterNameInput(it)
                    viewModel.clearFieldError("lastName")
                },
                label = { Text("Фамилия") },
                isError = fieldErrors.lastName != null,
                supportingText = { fieldErrors.lastName?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
        }

        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = {
                    middleName = filterNameInput(it)
                    viewModel.clearFieldError("middleName")
                },
                label = { Text("Отчество") },
                isError = fieldErrors.middleName != null,
                supportingText = { fieldErrors.middleName?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
        }

        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                label = { Text("Дата рождения") },
                placeholder = { Text("ГГГГ-ММ-ДД") },
                isError = fieldErrors.birthDate != null,
                supportingText = { fieldErrors.birthDate?.let { Text(it) } },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Выбрать дату")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = { TextButton(onClick = { showDatePicker = false }) { Text("OK") } },
                    dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        item {
            // Маппинг для отображения на русском
            val genderDisplayMap = mapOf(
                "MALE" to "Мужской",
                "FEMALE" to "Женский"
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = genderDisplayMap[gender] ?: gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    enabled = !uiState.isLoading
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    // Список с русскими названиями
                    listOf(
                        "MALE" to "Мужчина",
                        "FEMALE" to "Женщина"
                    ).forEach { (value, displayName) ->
                        DropdownMenuItem(
                            text = { Text(displayName) },
                            onClick = {
                                gender = value  // сохраняем MALE/FEMALE
                                genderExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            var groupExpanded by remember { mutableStateOf(false) }
            val groups = uiState.availableGroups
            val selectedGroup = groups.find { it.id == selectedGroupId }

            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = { groupExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedGroup?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    isError = fieldErrors.group != null,
                    supportingText = { fieldErrors.group?.let { Text(it) } },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    enabled = !uiState.isLoading
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
                                viewModel.clearFieldError("group")
                                groupExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = login,
                onValueChange = {
                    login = filterLatinLettersAndDigits(it)
                    viewModel.clearFieldError("login")
                },
                label = { Text("Логин") },
                isError = fieldErrors.login != null,
                supportingText = { fieldErrors.login?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it.replace(" ", "")
                    viewModel.clearFieldError("password")
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                isError = fieldErrors.password != null,
                supportingText = { fieldErrors.password?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.replace(" ", "")
                    viewModel.clearFieldError("email")
                },
                label = { Text("Email") },
                placeholder = { Text("example@gmail.com") },
                isError = fieldErrors.email != null,
                supportingText = { fieldErrors.email?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    val filtered = filterPhoneInput(it)
                    phoneNumber = filtered
                    viewModel.clearFieldError("phone")
                },
                label = { Text("Телефон") },
                placeholder = { Text("8XXXXXXXXXX") },
                isError = fieldErrors.phone != null,
                supportingText = { fieldErrors.phone?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    viewModel.validateAndRegister(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = selectedGroupId,
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber
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
        item {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Назад")
                }
            }
        }

        item {
            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}