package ci.nsu.moble.main.ui

import android.app.DatePickerDialog
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val nameRegex     = Regex("^[A-Za-zА-Яа-яЁё]{2,30}$")
    val loginRegex    = Regex("^[a-zA-Z0-9_]{4,20}$")
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$")

    var firstName    by rememberSaveable { mutableStateOf("") }
    var lastName     by rememberSaveable { mutableStateOf("") }
    var middleName   by rememberSaveable { mutableStateOf("") }
    var birthDate    by rememberSaveable { mutableStateOf("") }
    var login        by rememberSaveable { mutableStateOf("") }
    var password     by rememberSaveable { mutableStateOf("") }
    var email        by rememberSaveable { mutableStateOf("") }
    var phoneNumber  by rememberSaveable { mutableStateOf("") }

    var selectedGender    by rememberSaveable { mutableStateOf("") }
    var selectedGroupId   by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedGroupName by rememberSaveable { mutableStateOf("") }
    var genderExpanded    by remember { mutableStateOf(false) }
    var groupExpanded     by remember { mutableStateOf(false) }

    val genders = listOf("Мужской", "Женский")

    // DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            birthDate = "%04d-%02d-%02d".format(year, month + 1, day)
        },
        calendar.get(Calendar.YEAR) - 18,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        // Нельзя выбрать дату в будущем
        datePicker.maxDate = System.currentTimeMillis()
    }

    LaunchedEffect(Unit) { viewModel.loadGroups() }

    val isFormValid =
        nameRegex.matches(firstName) &&
                nameRegex.matches(lastName) &&
                loginRegex.matches(login) &&
                passwordRegex.matches(password) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                phoneNumber.length >= 10 &&
                birthDate.isNotEmpty() &&
                selectedGroupId != null &&
                selectedGender.isNotEmpty()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it.filter(Char::isLetter) },
                label = { Text("Имя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it.filter(Char::isLetter) },
                label = { Text("Фамилия") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it.filter(Char::isLetter) },
                label = { Text("Отчество") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Дата рождения") },
                placeholder = { Text("Выберите дату") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                            contentDescription = "Выбрать дату"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Пол
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                    },
                    singleLine = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genders.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender) },
                            onClick = { selectedGender = gender; genderExpanded = false }
                        )
                    }
                }
            }

            // Группа
            ExposedDropdownMenuBox(
                expanded = groupExpanded,
                onExpandedChange = { groupExpanded = !groupExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGroupName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded)
                    },
                    singleLine = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = groupExpanded,
                    onDismissRequest = { groupExpanded = false }
                ) {
                    state.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupId = group.id
                                selectedGroupName = group.name
                                groupExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = login,
                onValueChange = { login = it.filter { c -> c.isLetterOrDigit() || c == '_' } },
                label = { Text("Логин") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it.replace(" ", "") },
                label = { Text("Пароль(должна быть хотя-бы одна заглавная буква и одна цифра)") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.replace(" ", "") },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it.filter(Char::isDigit) },
                label = { Text("Телефон") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            Button(
                enabled = isFormValid && !state.isLoading,
                onClick = {
                    viewModel.register(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = selectedGender,
                        groupId = selectedGroupId,
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber,
                        onSuccess = onBackToLogin
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад ко входу")
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}