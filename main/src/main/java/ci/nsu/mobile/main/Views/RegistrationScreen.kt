@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ViewModels.RegistrationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val groups by viewModel.groups.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val middleName by viewModel.middleName.collectAsState()
    val birthDate by viewModel.birthDate.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
        viewModel.registerSuccess.collect {
            onRegisterSuccess()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Поля ввода (персональные данные + контактные + логин/пароль)
        // Для краткости покажу один пример, полный список полей легко добавить
        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = viewModel::updateFirstName,
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = viewModel::updateLastName,
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = middleName,
                onValueChange = viewModel::updateMiddleName,
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Дата рождения") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Выбрать дату")
                    }
                }
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDateSelected = { year, month, day ->
                        viewModel.updateBirthDate("$year-${month + 1}-$day")
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
        item {
            Text("Пол", modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == "male",
                        onClick = { viewModel.updateGender("male") }
                    )
                    Text("Мужской", modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == "female",
                        onClick = { viewModel.updateGender("female") }
                    )
                    Text("Женский", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
        item {
            OutlinedTextField(
                value = login,
                onValueChange = viewModel::updateLogin,
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phone,
                onValueChange = viewModel::updatePhone,
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            // Выбор группы
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = groups.find { it.id == viewModel.groupId.value }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Группа") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                viewModel.updateGroupId(group.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        item {
            if (isLoading) CircularProgressIndicator()
            else Button(
                onClick = viewModel::register,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Зарегистрироваться") }
        }
        if (errorMessage != null) {
            item { Text(errorMessage!!, color = Color.Red) }
        }
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = Date(millis)
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val parts = format.format(date).split("-")
                    onDateSelected(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}