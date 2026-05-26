package ci.nsu.mobile.main.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.Color

import ci.nsu.mobile.main.DataModels.PersonDto
import ci.nsu.mobile.main.DataModels.RegisterRequest
import ci.nsu.mobile.main.ViewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
){
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedGroupId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedGroupName by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearError()
        viewModel.loadGroups()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text("Registration")
        }
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Patronymic") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Birth day") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            TextField(
                value = selectedGroupName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Group") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
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

        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )

        Button(
            onClick = {
                val cleanLogin = login.trim()
                val cleanEmail = email.trim()
                val cleanPassword = password

                when {
                    cleanLogin.isBlank() -> {
                        viewModel.setValidationError("Enter login")
                        return@Button
                    }
                    cleanPassword.isBlank() -> {
                        viewModel.setValidationError("Enter password")
                        return@Button
                    }
                    !cleanEmail.contains("@") || cleanEmail.count { it == '@' } != 1 || !cleanEmail.substringAfter("@").contains(".") -> {
                        viewModel.setValidationError("Enter email")
                        return@Button
                    }
                    firstName.isBlank() -> {
                        viewModel.setValidationError("Enter name")
                        return@Button
                    }
                    lastName.isBlank() -> {
                        viewModel.setValidationError("Enter surname")
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
                    onNavigateBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Register")
            }
        }

        viewModel.error?.let {
            Text(it, color = Color.Red)
        }
    }
}