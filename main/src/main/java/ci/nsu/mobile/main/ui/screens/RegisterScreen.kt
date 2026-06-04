package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.viewmodels.AuthState
import ci.nsu.mobile.main.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var groupExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf(0) }
    var selectedGroupName by remember { mutableStateOf("Выберите группу") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val authState by viewModel.authState.collectAsState()
    val groups by viewModel.groups.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadGroups() }
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) { viewModel.resetState(); onRegisterSuccess() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = lastName, onValueChange = { lastName = it },
            label = { Text("Фамилия *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = firstName, onValueChange = { firstName = it },
            label = { Text("Имя *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = middleName, onValueChange = { middleName = it },
            label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата рождения *") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Выбрать дату")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                            calendar.timeInMillis = millis
                            birthDate = String.format("%04d-%02d-%02d",
                                calendar.get(java.util.Calendar.YEAR),
                                calendar.get(java.util.Calendar.MONTH) + 1,
                                calendar.get(java.util.Calendar.DAY_OF_MONTH)
                            )
                        }
                        showDatePicker = false
                    }) { Text("Выбрать") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = genderExpanded, onExpandedChange = { genderExpanded = it }) {
            OutlinedTextField(
                value = if (gender == "MALE") "Мужской" else "Женский",
                onValueChange = {}, readOnly = true, label = { Text("Пол *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                DropdownMenuItem(text = { Text("Мужской") }, onClick = { gender = "MALE"; genderExpanded = false })
                DropdownMenuItem(text = { Text("Женский") }, onClick = { gender = "FEMALE"; genderExpanded = false })
            }
        }
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = groupExpanded, onExpandedChange = { groupExpanded = it }) {
            OutlinedTextField(
                value = selectedGroupName, onValueChange = {}, readOnly = true,
                label = { Text("Группа *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = groupExpanded, onDismissRequest = { groupExpanded = false }) {
                if (groups.isEmpty()) DropdownMenuItem(text = { Text("Загрузка...") }, onClick = {})
                else groups.forEach { group ->
                    DropdownMenuItem(text = { Text(group.name) }, onClick = {
                        selectedGroupId = group.id; selectedGroupName = group.name; groupExpanded = false
                    })
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = login, onValueChange = { login = it },
            label = { Text("Логин *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text("Пароль *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Email *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = phone, onValueChange = { phone = it },
            label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(Modifier.height(16.dp))

        if (authState is AuthState.Error)
            Text((authState as AuthState.Error).message, color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 12.dp))

        Button(
            onClick = {
                viewModel.register(login, password, email, phone,
                    firstName, lastName, middleName, birthDate, gender, selectedGroupId)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading)
                CircularProgressIndicator(modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            else Text("Зарегистрироваться")
        }
        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onNavigateToLogin) { Text("Уже есть аккаунт? Войти") }
    }
}