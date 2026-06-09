package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.ui.viewmodel.AuthState
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val genders = listOf("MALE", "FEMALE")
    var expandedGender by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(genders[0]) }

    var expandedGroup by remember { mutableStateOf(false) }
    val groups by viewModel.groups.collectAsState()
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    val state by viewModel.registerState.collectAsState()

    fun formatDateFromMillis(millis: Long?): String {
        return if (millis != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.format(calendar.time)
        } else {
            ""
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.resetStates()
            onBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Личные данные", style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = birthDate,
            onValueChange = {}, // Ручной ввод запрещён
            readOnly = true,
            label = { Text("Дата рождения") },
            placeholder = { Text(birthDate.ifEmpty { "Нажмите на иконку " }) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        androidx.compose.material.icons.Icons.Default.Event,
                        contentDescription = "Открыть календарь"
                    )
                }
            }
        )

        // 🟢 ДИАЛОГ КАЛЕНДАРЯ
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                // Используем Calendar для совместимости с API 24+
                                val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                                birthDate = sdf.format(java.util.Date(millis))
                            }
                            showDatePicker = false
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Выбор пола
        ExposedDropdownMenuBox(expanded = expandedGender, onExpandedChange = { expandedGender = !expandedGender }) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedGender, onDismissRequest = { expandedGender = false }) {
                genders.forEach { gender ->
                    DropdownMenuItem(text = { Text(gender) }, onClick = {
                        selectedGender = gender
                        expandedGender = false
                    })
                }
            }
        }

        // Выбор группы
        val displayGroups = if (groups.isEmpty()) listOf(GroupDto(1, "Загрузка...")) else groups

        ExposedDropdownMenuBox(expanded = expandedGroup, onExpandedChange = { if (groups.isNotEmpty()) expandedGroup = !expandedGroup }) {
            OutlinedTextField(
                value = selectedGroup?.groupName ?: "Выберите группу",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedGroup, onDismissRequest = { expandedGroup = false }) {
                displayGroups.forEach { group ->
                    DropdownMenuItem(text = { Text(group.groupName) }, onClick = {
                        selectedGroup = group
                        expandedGroup = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedGroup != null && birthDate.isNotBlank()) {
                    val person = PersonDto(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = if (middleName.isBlank()) null else middleName,
                        birthDate = birthDate,
                        gender = selectedGender,
                        groupId = selectedGroup!!.groupId
                    )
                    viewModel.register(login, password, email, person)
                }
            },
            enabled = state !is AuthState.Loading && login.isNotBlank() && password.isNotBlank() && selectedGroup != null && birthDate.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }

        TextButton(onClick = onBackToLogin) { Text("Уже есть аккаунт? Войти") }

        if (state is AuthState.Error) {
            Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}