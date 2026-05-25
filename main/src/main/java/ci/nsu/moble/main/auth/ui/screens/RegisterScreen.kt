package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.auth.data.models.*
import ci.nsu.mobile.auth.data.repository.ApiResult
import ci.nsu.mobile.auth.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val groups by viewModel.groups.collectAsState()
    val groupsLoading by viewModel.groupsLoading.collectAsState()
    val registerState by viewModel.registerState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(registerState) {
        if (registerState is ApiResult.Success) {
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
        item {
            Text("Регистрация", fontSize = 24.sp)
        }
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Пол и группа — пропустим для краткости
        item {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            if (registerState is ApiResult.Error) {
                Text(
                    text = (registerState as ApiResult.Error).message,
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
                        groupId = selectedGroupId!!
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
                enabled = !isLoading && firstName.isNotBlank() && lastName.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Зарегистрироваться")
                }
            }
        }
        item {
            TextButton(onClick = onBackToLogin) {
                Text("Уже есть аккаунт? Войти")
            }
        }
    }
}