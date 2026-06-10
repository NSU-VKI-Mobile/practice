package ci.nsu.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(viewModel: AuthViewModel, onBackToLogin: () -> Unit) {
    LaunchedEffect(Unit) {
        android.util.Log.d("API_TEST", "Экран регистрации открыт, вызываю loadGroups()")
        viewModel.loadGroups()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Регистрация") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Личные данные", style = MaterialTheme.typography.titleMedium)

                // ИМЯ
                OutlinedTextField(
                    value = viewModel.firstName,
                    onValueChange = { viewModel.firstName = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                // ФАМИЛИЯ
                OutlinedTextField(
                    value = viewModel.lastName,
                    onValueChange = { viewModel.lastName = it },
                    label = { Text("Фамилия") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                // ДАТА РОЖДЕНИЯ (Изменено под формат ДД.ММ.ГГГГ и тип Text для ввода точек)
                OutlinedTextField(
                    value = viewModel.birthDate,
                    onValueChange = { viewModel.birthDate = it },
                    label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                // Выбор пола
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Пол: ")
                    RadioButton(
                        selected = viewModel.gender == "MALE",
                        onClick = { viewModel.gender = "MALE" }
                    )
                    Text("М")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = viewModel.gender == "FEMALE",
                        onClick = { viewModel.gender = "FEMALE" }
                    )
                    Text("Ж")
                }

                // Выпадающий список групп
                var expanded by remember { mutableStateOf(false) }
                val selectedGroup = viewModel.groups.find { it.id == viewModel.selectedGroupId }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedGroup?.name ?: "Выберите группу",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Группа") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
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

                Spacer(modifier = Modifier.height(16.dp))
                Text("Данные аккаунта", style = MaterialTheme.typography.titleMedium)

                // ЛОГИН
                OutlinedTextField(
                    value = viewModel.loginText,
                    onValueChange = { viewModel.loginText = it },
                    label = { Text("Логин") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                // ПАРОЛЬ
                OutlinedTextField(
                    value = viewModel.passwordText,
                    onValueChange = { viewModel.passwordText = it },
                    label = { Text("Пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                // EMAIL
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                // ТЕЛЕФОН
                OutlinedTextField(
                    value = viewModel.phone,
                    onValueChange = { viewModel.phone = it },
                    label = { Text("Телефон") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )

                // Ошибки и индикатор загрузки
                viewModel.errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (viewModel.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Button(
                        onClick = { viewModel.onRegisterClick() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Зарегистрироваться")
                    }
                }

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад к входу")
                }
            }
        }
    }
}

