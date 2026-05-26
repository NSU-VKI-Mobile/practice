package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

import ci.nsu.moble.main.data.models.PersonDto
import ci.nsu.moble.main.data.models.RegisterRequest
import ci.nsu.moble.main.data.repository.AuthApiResult
import ci.nsu.moble.main.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel
) {
    // поля для ввода
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var groupName by remember { mutableStateOf("") }          // группа текстом
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // для календаря
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // когда выбираем дату
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            birthDate = dateFormat.format(Date(millis))
            showDatePicker = false
        }
    }

    // если регистрация успешна — закрываем экран
    LaunchedEffect(registerState) {
        if (registerState is AuthApiResult.Success) {
            onRegisterSuccess()
            viewModel.clearStates()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Регистрация", fontSize = 24.sp) }

        // фамилия
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // имя
        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // отчество
        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // дата рождения (с календарём)
        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Дата рождения") },
                placeholder = { Text("выберите дату") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "выбрать дату")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // пол (чипсы)
        item {
            Text("Пол", fontSize = 14.sp)
            Row {
                FilterChip(
                    selected = gender == "MALE",
                    onClick = { gender = "MALE" },
                    label = { Text("Мужской") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = gender == "FEMALE",
                    onClick = { gender = "FEMALE" },
                    label = { Text("Женский") }
                )
            }
        }

        // группа (просто текстовое поле)
        item {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Группа") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // логин
        item {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // пароль
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // email
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // телефон
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // кнопка и ошибки
        item {
            if (registerState is AuthApiResult.Error) {
                Text(
                    text = (registerState as AuthApiResult.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = {
                    val person = PersonDto(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = 1  // временно, пока группа не привязана к id
                    )
                    val request = RegisterRequest(
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        person = person
                    )
                    viewModel.register(request)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Зарегистрироваться")
            }
        }

        // назад ко входу
        item {
            TextButton(onClick = onBackToLogin) {
                Text("Уже есть аккаунт? Войти")
            }
        }
    }

    //  выбора даты
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        birthDate = dateFormat.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}