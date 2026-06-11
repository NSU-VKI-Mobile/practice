package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.viewmodel.RegistrationViewModel
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalMaterial3Api::class, InternalSerializationApi::class)
@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: RegistrationViewModel = viewModel()

    LaunchedEffect(viewModel.registrationSuccess) {
        if (viewModel.registrationSuccess) onRegistrationSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(viewModel.firstName, { viewModel.firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.lastName, { viewModel.lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.middleName, { viewModel.middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.birthDate, { viewModel.birthDate = it }, label = { Text("Дата рождения") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.gender, { viewModel.gender = it }, label = { Text("Пол") }, modifier = Modifier.fillMaxWidth())

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = viewModel.groups.find { it.id == viewModel.selectedGroupId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    viewModel.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                viewModel.selectedGroupId = group.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(viewModel.login, { viewModel.login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.password, { viewModel.password = it }, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.email, { viewModel.email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.phoneNumber, { viewModel.phoneNumber = it }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth())

            viewModel.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = { viewModel.performRegistration() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else Text("Зарегистрироваться")
            }
        }
    }
}