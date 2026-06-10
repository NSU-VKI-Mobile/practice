package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var showDatePicker by remember { mutableStateOf(false) }

    var gender by remember { mutableStateOf("MALE") }
    var genderExpanded by remember { mutableStateOf(false) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var groupId by remember { mutableStateOf(0) }
    var groupName by remember { mutableStateOf("") }
    var groupsExpanded by remember { mutableStateOf(false) }

    val groups by vm.groups.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadGroups()
    }

    if (showDatePicker) {

        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val formatter = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            )

                            birthDate = formatter.format(Date(millis))
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата рождения") },
            placeholder = { Text("Выберите дату") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        Button(
            onClick = {
                showDatePicker = true
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text("Выбрать дату")
        }

        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = {
                if (!loading) {
                    genderExpanded = !genderExpanded
                }
            }
        ) {
            OutlinedTextField(
                value = when (gender) {
                    "MALE" -> "Мужской"
                    "FEMALE" -> "Женский"
                    else -> gender
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !loading
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = {
                    genderExpanded = false
                }
            ) {

                DropdownMenuItem(
                    text = { Text("Мужской") },
                    onClick = {
                        gender = "MALE"
                        genderExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Женский") },
                    onClick = {
                        gender = "FEMALE"
                        genderExpanded = false
                    }
                )
            }
        }

        ExposedDropdownMenuBox(
            expanded = groupsExpanded,
            onExpandedChange = {
                if (!loading) {
                    groupsExpanded = !groupsExpanded
                }
            }
        ) {
            OutlinedTextField(
                value = groupName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !loading
            )

            ExposedDropdownMenu(
                expanded = groupsExpanded,
                onDismissRequest = {
                    groupsExpanded = false
                }
            ) {

                groups.forEach { group ->

                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            groupId = group.id
                            groupName = group.name
                            groupsExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Телефон") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        )

        error?.let {
            Text(
                text = it,
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

                vm.register(
                    request = request,
                    onSuccess = onSuccess
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled =
                !loading &&
                        login.isNotBlank() &&
                        password.isNotBlank() &&
                        firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        birthDate.isNotBlank() &&
                        groupId != 0
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
