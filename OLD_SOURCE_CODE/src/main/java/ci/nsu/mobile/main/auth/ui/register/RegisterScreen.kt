package ci.nsu.mobile.main.auth.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.auth.ui.components.ProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val viewModel: RegisterViewModel  = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val form = uiState.formState
    val isLoading = uiState.registerState is RegisterState.Loading

    LaunchedEffect(uiState.registerState) {
        if (uiState.registerState is RegisterState.Success && !uiState.hasNavigated) {
            onRegisterSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Info Section
            Text(
                text = "Личные данные",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = form.lastName,
                onValueChange = viewModel::updateLastName,
                label = { Text("Фамилия *") },
                isError = form.lastNameError != null,
                supportingText = { form.lastNameError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.firstName,
                onValueChange = viewModel::updateFirstName,
                label = { Text("Имя *") },
                isError = form.firstNameError != null,
                supportingText = { form.firstNameError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.middleName,
                onValueChange = viewModel::updateMiddleName,
                label = { Text("Отчество") },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.birthDate,
                onValueChange = viewModel::updateBirthDate,
                label = { Text("Дата рождения (ГГГГ-ММ-ДД) *") },
                isError = form.birthDateError != null,
                supportingText = { form.birthDateError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            // Gender Dropdown
            var expanded by remember { mutableStateOf(false) }
            val genders = listOf("MALE", "FEMALE")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = form.gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = form.genderError != null,
                    supportingText = { form.genderError?.let { Text(it) } },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genders.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender) },
                            onClick = {
                                viewModel.updateGender(gender)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Group Dropdown
            var groupExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = { groupExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.groups.find { it.id == form.groupId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                    isError = form.groupError != null,
                    supportingText = { form.groupError?.let { Text(it) } },
                    enabled = !isLoading && !uiState.groupsLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = groupExpanded,
                    onDismissRequest = { groupExpanded = false }
                ) {
                    if (uiState.groupsLoading) {
                        DropdownMenuItem(
                            text = { Text("Загрузка...") },
                            onClick = { }
                        )
                    } else {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account Info Section
            Text(
                text = "Данные для входа",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = form.login,
                onValueChange = viewModel::updateLogin,
                label = { Text("Логин *") },
                isError = form.loginError != null,
                supportingText = { form.loginError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Пароль *") },
                isError = form.passwordError != null,
                supportingText = { form.passwordError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email *") },
                isError = form.emailError != null,
                supportingText = { form.emailError?.let { Text(it) } },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.phoneNumber,
                onValueChange = viewModel::updatePhoneNumber,
                label = { Text("Телефон") },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { viewModel.register() },
                enabled = !isLoading
            ){
                Text("Зарегистрироваться")
            }

            TextButton(
                onClick = onNavigateBack,
                enabled = !isLoading
            ) {
                Text("Уже есть аккаунт? Войти")
            }

            if (uiState.registerState is RegisterState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (uiState.registerState as RegisterState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        if (isLoading || uiState.groupsLoading) {
            ProgressBar()
        }
    }
}