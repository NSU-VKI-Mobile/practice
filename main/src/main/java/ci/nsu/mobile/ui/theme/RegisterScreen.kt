package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.model.*
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: AuthViewModel = viewModel()

    val groupsState = viewModel.groups.observeAsState()
    val groups = groupsState.value ?: emptyList()

    val error by viewModel.error.observeAsState()

    // Группа
    var groupExpanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    // Пол
    var genderExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    val genderOptions = listOf("Мужской", "Женский")

    // Дата рождения
    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Остальные поля
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Календарь
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    // DatePicker диалог
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            birthDate = format.format(date)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            "Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // Имя
        OutlinedTextField(firstName, { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Фамилия
        OutlinedTextField(lastName, { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Отчество
        OutlinedTextField(middleName, { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // ДАТА РОЖДЕНИЯ (с календарем)
        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            label = { Text("Дата рождения") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Выбрать дату"
                    )
                }
            }
        )
        Spacer(Modifier.height(8.dp))

        // ПОЛ (выпадающий список)
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedGender.ifEmpty { "Выберите пол" },
                onValueChange = {},
                label = { Text("Пол") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genderOptions.forEach { genderOption ->
                    DropdownMenuItem(
                        text = { Text(genderOption) },
                        onClick = {
                            selectedGender = genderOption
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ГРУППА (выпадающий список)
        ExposedDropdownMenuBox(
            expanded = groupExpanded,
            onExpandedChange = { groupExpanded = !groupExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedGroup?.name ?: "Выберите группу",
                onValueChange = {},
                label = { Text("Группа") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = { groupExpanded = false }
            ) {
                if (groups.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Загрузка...") },
                        onClick = {}
                    )
                } else {
                    groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroup = group
                                groupExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Логин
        OutlinedTextField(login, { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Пароль
        OutlinedTextField(password, { password = it }, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Email
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Телефон
        OutlinedTextField(phone, { phone = it }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName,
                    birthDate = birthDate,
                    gender = selectedGender,
                    groupId = selectedGroup?.id ?: 1
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
                    navController.popBackStack()
                }
            }
        ) {
            Text("Зарегистрироваться")
        }

        error?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}