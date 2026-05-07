package ci.nsu.mobile.main.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.model.PersonDto
import ci.nsu.mobile.main.model.RegisterRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
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
            .padding(16.dp)
    ) {

        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

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
                if (selectedGroupId == null) return@Button

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
                    phoneNumber = phone,
                    roleId = 1,
                    authAllowed = true,
                    person = person
                )

                viewModel.register(request) {
                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
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