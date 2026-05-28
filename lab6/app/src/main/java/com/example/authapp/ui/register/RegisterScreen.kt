package com.example.authapp.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.authapp.ui.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Загружаем группы при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    // Когда регистрация прошла — возвращаемся на логин
    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) onRegistered()
    }

    // Состояние выпадающего списка групп
    var groupExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                // verticalScroll — прокрутка, т.к. полей много
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = uiState.regLastName,
                onValueChange = { viewModel.onRegLastNameChanged(it) },
                label = { Text("Фамилия *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regFirstName,
                onValueChange = { viewModel.onRegFirstNameChanged(it) },
                label = { Text("Имя *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regMiddleName,
                onValueChange = { viewModel.onRegMiddleNameChanged(it) },
                label = { Text("Отчество") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regBirthDate,
                onValueChange = { viewModel.onRegBirthDateChanged(it) },
                label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
                placeholder = { Text("2000-01-15") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Выбор пола
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.regGender == "M",
                        onClick = { viewModel.onRegGenderChanged("M") }
                    )
                    Text("Мужской")
                }
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.regGender == "F",
                        onClick = { viewModel.onRegGenderChanged("F") }
                    )
                    Text("Женский")
                }
            }

            // Выпадающий список групп
            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = { groupExpanded = !groupExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.groups.find { it.id == uiState.regGroupId }?.name ?: "Выберите группу",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = groupExpanded,
                    onDismissRequest = { groupExpanded = false }
                ) {
                    uiState.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                viewModel.onRegGroupChanged(group.id)
                                groupExpanded = false
                            }
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            OutlinedTextField(
                value = uiState.regLogin,
                onValueChange = { viewModel.onRegLoginChanged(it) },
                label = { Text("Логин *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regPassword,
                onValueChange = { viewModel.onRegPasswordChanged(it) },
                label = { Text("Пароль *") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regEmail,
                onValueChange = { viewModel.onRegEmailChanged(it) },
                label = { Text("Email *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.regPhone,
                onValueChange = { viewModel.onRegPhoneChanged(it) },
                label = { Text("Телефон") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Ошибка
            uiState.errorMessage?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.register() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Зарегистрироваться", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
