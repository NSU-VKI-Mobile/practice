package ci.nsu.mobile.main.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.DatePickerDialog
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedGroupId by remember { mutableIntStateOf(-1) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Личные данные",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Date picker
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                label = { Text("Дата рождения") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Button(
                onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            birthDate = "$year-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать дату рождения")
            }

            // Gender dropdown
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Пол") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    listOf("male", "female").forEach { genderOption ->
                        DropdownMenuItem(
                            text = { Text(if (genderOption == "male") "Мужской" else "Женский") },
                            onClick = {
                                gender = genderOption
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Учетные данные",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Group dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.groups.find { it.id == selectedGroupId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupId = group.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.register(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = selectedGroupId,
                        login = login,
                        password = password,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading &&
                        firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        birthDate.isNotBlank() &&
                        gender.isNotBlank() &&
                        selectedGroupId != -1 &&
                        login.isNotBlank() &&
                        password.isNotBlank() &&
                        email.isNotBlank() &&
                        phoneNumber.isNotBlank()
            ) {
                if (uiState.isLoading) {
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
}

