package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var selectedGroupId by rememberSaveable { mutableStateOf(0) }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    val groups by viewModel.groups.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val success by viewModel.success.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            viewModel.clearSuccess()
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Дата рождения (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            SelectorField(
                label = "Пол",
                value = gender.ifBlank { "Выберите пол" },
                options = listOf("Male", "Female"),
                enabled = !loading,
                onOptionSelected = { gender = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            SelectorField(
                label = "Группа",
                value = groups.firstOrNull { it.id == selectedGroupId }?.name ?: "Выберите группу",
                options = groups.map { it.name },
                enabled = !loading && groups.isNotEmpty(),
                onOptionSelected = { selectedName ->
                    selectedGroupId = groups.first { it.name == selectedName }.id
                }
            )

            if (groups.isEmpty() && !loading) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        viewModel.clearError()
                        viewModel.loadGroups()
                    }
                ) {
                    Text("Повторить загрузку групп")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        viewModel.register(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            middleName = middleName.trim(),
                            birthDate = birthDate.trim(),
                            gender = gender,
                            groupId = selectedGroupId,
                            login = login.trim(),
                            password = password,
                            email = email.trim(),
                            phoneNumber = phoneNumber.trim()
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = groups.isNotEmpty()
                ) {
                    Text("Зарегистрироваться")
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    viewModel.clearError()
                    onNavigateToLogin()
                }
            ) {
                Text("Уже есть аккаунт? Войти")
            }
        }
    }
}

@Composable
private fun SelectorField(
    label: String,
    value: String,
    options: List<String>,
    enabled: Boolean,
    onOptionSelected: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedButton(
        onClick = { expanded = true },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            text = value,
            modifier = Modifier.fillMaxWidth()
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                }
            )
        }
    }
}
