package ci.nsu.mobile.main.presentation.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.presentation.components.LoadingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as DepositApplication
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(application.locator.authRepository)
    )

    val uiState by viewModel.uiState.collectAsState()
    var groupExpanded by remember { mutableStateOf(false) }
    val genders = listOf("MALE" to "Мужской", "FEMALE" to "Женский")
    var genderExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Регистрация") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.firstName,
                onValueChange = { viewModel.updateFirstName(it) },
                label = { Text("Имя *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = { viewModel.updateLastName(it) },
                label = { Text("Фамилия *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.middleName,
                onValueChange = { viewModel.updateMiddleName(it) },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.birthDate,
                onValueChange = { viewModel.updateBirthDate(it) },
                label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
                placeholder = { Text("2000-01-01") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = genders.find { it.first == uiState.gender }?.second ?: "Выберите пол",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genders.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                viewModel.updateGender(value)
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = { groupExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.groups.find { it.id == uiState.groupId }?.name ?: "Выберите группу *",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = groupExpanded,
                    onDismissRequest = { groupExpanded = false }
                ) {
                    uiState.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                viewModel.updateGroupId(group.id)
                                groupExpanded = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = uiState.login,
                onValueChange = { viewModel.updateLogin(it) },
                label = { Text("Логин *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Пароль *") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { viewModel.updatePhone(it) },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            uiState.error?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                LoadingButton(
                    onClick = {
                        viewModel.register {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    },
                    isLoading = uiState.isLoading,
                    text = "Зарегистрироваться",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}