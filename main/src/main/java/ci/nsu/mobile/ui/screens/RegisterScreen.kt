package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.data.model.*
import ci.nsu.mobile.ui.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: AuthViewModel, navController: NavController) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }

    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    var gender by remember { mutableStateOf("MALE") }

    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    LaunchedEffect(Unit) {
        vm.loadGroups()
    }

    LaunchedEffect(vm.isLoggedIn) {
        if (vm.isLoggedIn) {
            navController.navigate("main") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // ================= PERSON =================

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // ================= DATE PICKER (CLICK FIELD) =================

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        LaunchedEffect(isPressed) {
            if (isPressed) {
                showDatePicker = true
            }
        }

        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата рождения") },
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            trailingIcon = {
                TextButton(onClick = { showDatePicker = true }) {
                    Text("📅")
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        // ================= GENDER =================

        Text("Пол")

        Row {
            RadioButton(
                selected = gender == "MALE",
                onClick = { gender = "MALE" }
            )
            Text("Мужской")

            Spacer(Modifier.width(16.dp))

            RadioButton(
                selected = gender == "FEMALE",
                onClick = { gender = "FEMALE" }
            )
            Text("Женский")
        }

        Spacer(Modifier.height(8.dp))

        // ================= GROUP =================

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedGroup?.name ?: "Выбрать группу")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vm.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            selectedGroup = group
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ================= AUTH =================

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

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
                        groupId = selectedGroup?.id ?: 1
                    )
                )

                vm.register(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Уже есть аккаунт? Войти")
        }

        vm.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        if (vm.isLoading) {
            CircularProgressIndicator()
        }
    }

    // ================= DATE PICKER =================

    if (showDatePicker) {

        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis

                    if (millis != null) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        birthDate = sdf.format(Date(millis))
                    }

                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}