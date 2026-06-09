package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onSuccess: () -> Unit
) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("M") }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var groupId by remember { mutableStateOf(0) }
    var groupName by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val groups by vm.groups.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadGroups()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(firstName, { firstName = it }, label = { Text("Имя") }, enabled = !loading)
        OutlinedTextField(lastName, { lastName = it }, label = { Text("Фамилия") }, enabled = !loading)
        OutlinedTextField(middleName, { middleName = it }, label = { Text("Отчество") }, enabled = !loading)
        OutlinedTextField(birthDate, { birthDate = it }, label = { Text("Дата рождения") }, enabled = !loading)
        OutlinedTextField(gender, { gender = it }, label = { Text("Пол") }, enabled = !loading)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (!loading) expanded = !expanded }
        ) {

            OutlinedTextField(
                value = groupName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                modifier = Modifier.menuAnchor(),
                enabled = !loading
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                groups.forEach { g ->
                    DropdownMenuItem(
                        text = { Text(g.name) },
                        onClick = {
                            groupId = g.id
                            groupName = g.name
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(login, { login = it }, label = { Text("Логин") }, enabled = !loading)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            enabled = !loading,
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, enabled = !loading)
        OutlinedTextField(phone, { phone = it }, label = { Text("Телефон") }, enabled = !loading)

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phone,
                    roleId = 1,
                    authAllowed = true,
                    person = PersonDto(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = groupId
                    )
                )

                vm.register(request, onSuccess)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading && login.isNotEmpty() && password.isNotEmpty() && groupId != 0
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Зарегистрироваться")
            }
        }
    }
}