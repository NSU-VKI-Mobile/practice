package ci.nsu.mobile.main.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.model.PersonDto
import ci.nsu.mobile.main.model.RegisterRequest
import android.util.Patterns.EMAIL_ADDRESS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var selectedGroupName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearError()
        viewModel.loadGroups()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад"
                )
            }

            Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(firstName, { firstName = it }, label = { Text("Имя") })
        OutlinedTextField(lastName, { lastName = it }, label = { Text("Фамилия") })
        OutlinedTextField(middleName, { middleName = it }, label = { Text("Отчество") })
        OutlinedTextField(birthDate, { birthDate = it }, label = { Text("Дата рождения") })
        OutlinedTextField(gender, { gender = it }, label = { Text("Пол") })

        Spacer(Modifier.height(12.dp))

        // ================= GROUP DROPDOWN =================
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = selectedGroupName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.groupName) },
                        onClick = {
                            selectedGroupId = group.groupId
                            selectedGroupName = group.groupName
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(login, { login = it }, label = { Text("Логин") })
        OutlinedTextField(password, { password = it }, label = { Text("Пароль") })
        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(phone, { phone = it }, label = { Text("Телефон") })

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {

                val cleanLogin = login.trim()
                val cleanEmail = email.trim()
                val cleanPassword = password
                when {
                    cleanLogin.isBlank() -> {
                        viewModel.setValidationError("Введите логин")
                        return@Button
                    }
                    cleanPassword.isBlank() -> {
                        viewModel.setValidationError("Введите пароль")
                        return@Button
                    }
                    !cleanEmail.contains("@") || cleanEmail.count { it == '@' } != 1 || !cleanEmail.substringAfter("@").contains(".") -> {
                        viewModel.setValidationError("Введите email")
                        return@Button
                    }
                    firstName.isBlank() -> {
                        viewModel.setValidationError("Введите имя")
                        return@Button
                    }
                    lastName.isBlank() -> {
                        viewModel.setValidationError("Введите фамилия")
                        return@Button
                    }
                }

                val formattedBirthDate = if (birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    birthDate
                } else {
                    birthDate.split(".").reversed().joinToString("-").take(10)
                }

                val person = PersonDto(
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    middleName = middleName.ifBlank { "" },
                    birthDate =formattedBirthDate,
                    gender = gender.ifBlank { "other" },
                    groupId = selectedGroupId ?: 1
                )

                val request = RegisterRequest(
                    login = cleanLogin.trim(),
                    password = cleanPassword,
                    email = cleanEmail.trim(),
                    phoneNumber = phone.ifBlank { "" },
                    roleId = 1,
                    authAllowed = true,
                    person = person
                )

                viewModel.register(request) {
                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Зарегистрироваться")
            }

        }

        Spacer(Modifier.height(12.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }

        viewModel.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}