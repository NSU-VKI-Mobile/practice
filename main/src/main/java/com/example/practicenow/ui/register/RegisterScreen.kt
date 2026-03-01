package com.example.practicenow.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicenow.data.model.PersonDto
import com.example.practicenow.data.model.RegisterRequest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackToLogin: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { Text("Регистрация", style = MaterialTheme.typography.headlineMedium) }
        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth()) }

        item {
            Button(
                onClick = {
                    val person = PersonDto(firstName, lastName, "Отчество", "2000-01-01", "M", 1)
                    val request = RegisterRequest(login, password, email, "+79990000000", 1, true, person)
                    viewModel.register(request, onBackToLogin)
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Зарегистрироваться")
            }
        }
        item {
            TextButton(onClick = onBackToLogin, modifier = Modifier.fillMaxWidth()) {
                Text("Назад ко входу")
            }
        }
    }
}