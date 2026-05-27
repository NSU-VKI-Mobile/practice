package ci.nsu.mobile.main.ui.screens

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

    fun formatPhoneForDisplay(raw: String): String {
        var cleaned = raw.replace(Regex("[^\\d]"), "")
        if (cleaned.startsWith("7") && cleaned.length > 1) cleaned = cleaned.drop(1)
        if (cleaned.startsWith("8") && cleaned.length > 1) cleaned = cleaned.drop(1)
        if (cleaned.length > 10) cleaned = cleaned.take(10)

        return when (cleaned.length) {
            0 -> ""
            1 -> "+7 $cleaned"
            2 -> "+7 ${cleaned[0]}${cleaned[1]}"
            3 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]}"
            4 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}"
            5 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}"
            6 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}${cleaned[5]}"
            7 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}${cleaned[5]} ${cleaned[6]}"
            8 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}${cleaned[5]} ${cleaned[6]}${cleaned[7]}"
            9 -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}${cleaned[5]} ${cleaned[6]}${cleaned[7]}${cleaned[8]}"
            else -> "+7 ${cleaned[0]}${cleaned[1]}${cleaned[2]} ${cleaned[3]}${cleaned[4]}${cleaned[5]} ${cleaned[6]}${cleaned[7]}${cleaned[8]}${cleaned[9]}"
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
                label = { Text("Отчество (необязательно)") },
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
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = gender,
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
                    listOf("MALE", "FEMALE").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { gender = option; genderExpanded = false }
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
                    val digitsOnly = it.filter { it.isDigit() }
                    phoneNumber = if (digitsOnly.isNotEmpty()) formatPhoneForDisplay(digitsOnly) else ""
                    viewModel.clearFieldError("phone")
                },
                label = { Text("Телефон") },
                placeholder = { Text("+7 999 999 99 99") },
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
            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}