package com.example.userapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.userapp.viewmodel.RegisterViewModel
import com.example.userapp.ui.components.LoadingDialog
import com.example.userapp.ui.components.ErrorDialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import java.time.format.DateTimeFormatter
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.registerState.collectAsState()
    val groupsState by viewModel.groupsState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    val dateDialogState = rememberMaterialDialogState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccess()
            onRegisterSuccess()
        }
    }

    if (state.isLoading || groupsState.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        ErrorDialog(
            message = state.error!!,
            onDismiss = { viewModel.resetError() }
        )
    }

    // Date picker dialog
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Отмена")
        }
    ) {
        datepicker(
            initialDate = LocalDate(2000, 1, 1),
            title = "Выберите дату рождения"
        ) { date ->
            viewModel.updateField("birthDate", date.toString())
        }
    }

    // Gender selection dialog
    val genderDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = genderDialogState,
        buttons = {
            positiveButton("Выбрать")
            negativeButton("Отмена")
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Выберите пол", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = state.gender == "MALE",
                    onClick = { viewModel.selectGender("MALE") },
                    label = { Text("Мужской") }
                )
                FilterChip(
                    selected = state.gender == "FEMALE",
                    onClick = { viewModel.selectGender("FEMALE") },
                    label = { Text("Женский") }
                )
            }
        }
    }

    // Group selection dialog
    val groupDialogState = rememberMaterialDialogState()
    var selectedGroupName by remember { mutableStateOf("") }

    MaterialDialog(
        dialogState = groupDialogState,
        buttons = {
            positiveButton("Выбрать")
            negativeButton("Отмена")
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Выберите группу", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            groupsState.groups.forEach { group ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = state.groupId == group.id,
                        onClick = {
                            viewModel.selectGroup(group.id)
                            selectedGroupName = group.name
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(group.name)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Личные данные", style = MaterialTheme.typography.titleLarge) }

            item {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { viewModel.updateField("firstName", it) },
                    label = { Text("Имя *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.firstNameError != null,
                    supportingText = {
                        if (state.firstNameError != null) {
                            Text(state.firstNameError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { viewModel.updateField("lastName", it) },
                    label = { Text("Фамилия *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.lastNameError != null,
                    supportingText = {
                        if (state.lastNameError != null) {
                            Text(state.lastNameError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = state.middleName,
                    onValueChange = { viewModel.updateField("middleName", it) },
                    label = { Text("Отчество") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = state.birthDate,
                    onValueChange = {},
                    label = { Text("Дата рождения *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.birthDateError != null,
                    supportingText = {
                        if (state.birthDateError != null) {
                            Text(state.birthDateError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { dateDialogState.show() }) {
                            Icon(Icons.Default.Visibility, contentDescription = "Выбрать дату")
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = if (state.gender == "MALE") "Мужской" else if (state.gender == "FEMALE") "Женский" else "",
                    onValueChange = {},
                    label = { Text("Пол *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.genderError != null,
                    supportingText = {
                        if (state.genderError != null) {
                            Text(state.genderError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { genderDialogState.show() }) {
                            Icon(Icons.Default.Visibility, contentDescription = "Выбрать пол")
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = selectedGroupName,
                    onValueChange = {},
                    label = { Text("Группа *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.groupError != null,
                    supportingText = {
                        if (state.groupError != null) {
                            Text(state.groupError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { groupDialogState.show() }) {
                            Icon(Icons.Default.Visibility, contentDescription = "Выбрать группу")
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Text("Учетные данные", style = MaterialTheme.typography.titleLarge) }

            item {
                OutlinedTextField(
                    value = state.login,
                    onValueChange = { viewModel.updateField("login", it) },
                    label = { Text("Логин *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.loginError != null,
                    supportingText = {
                        if (state.loginError != null) {
                            Text(state.loginError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.updateField("password", it) },
                    label = { Text("Пароль *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.passwordError != null,
                    supportingText = {
                        if (state.passwordError != null) {
                            Text(state.passwordError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (showPassword) PasswordVisualTransformation() else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                            )
                        }
                    },
                    keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            item {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.updateField("email", it) },
                    label = { Text("Email *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.emailError != null,
                    supportingText = {
                        if (state.emailError != null) {
                            Text(state.emailError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }

            item {
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.updateField("phoneNumber", it) },
                    label = { Text("Телефон") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            item {
                Button(
                    onClick = { viewModel.register() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Зарегистрироваться")
                }
            }
        }
    }
}