package ci.nsu.moble.auth.ui.register

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.moble.auth.ui.register.RegisterViewModel
import ci.nsu.moble.domain.models.Group
import kotlinx.serialization.InternalSerializationApi
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, InternalSerializationApi::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var selectedGroupId by remember { mutableStateOf(0) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val registerState by viewModel.registerState.collectAsState()
    val groupsState by viewModel.groupsState.collectAsState()
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            birthDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(registerState) {
        if (registerState is RegisterViewModel.RegisterState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    val groups: List<Group> = when (val state = groupsState) {
        is RegisterViewModel.GroupsState.Success -> state.groups
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = birthDate, onValueChange = {}, label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
            modifier = Modifier.fillMaxWidth(), readOnly = true, enabled = true,
            trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.CalendarMonth, contentDescription = "Выбрать дату") } }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Пол", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(vertical = 4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "MALE", onClick = { gender = "MALE" })
                Text("Мужской")
            }
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "FEMALE", onClick = { gender = "FEMALE" })
                Text("Женский")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (val state = groupsState) {
            is RegisterViewModel.GroupsState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }
            is RegisterViewModel.GroupsState.Success -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = groups.find { it.groupId == selectedGroupId }?.groupName ?: "Выберите группу",
                        onValueChange = {}, readOnly = true, label = { Text("Группа *") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown, contentDescription = "Выбрать группу")
                            }
                        }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
                        groups.forEach { group ->
                            DropdownMenuItem(text = { Text(group.groupName) }, onClick = { selectedGroupId = group.groupId; expanded = false })
                        }
                    }
                }
            }
            is RegisterViewModel.GroupsState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = registerState is RegisterViewModel.RegisterState.Error)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), isError = registerState is RegisterViewModel.RegisterState.Error)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), isError = registerState is RegisterViewModel.RegisterState.Error)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Телефон *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), isError = registerState is RegisterViewModel.RegisterState.Error)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.register(firstName, lastName, middleName, birthDate, gender, selectedGroupId, login, password, email, phoneNumber) },
            modifier = Modifier.fillMaxWidth(),
            enabled = registerState !is RegisterViewModel.RegisterState.Loading
        ) {
            if (registerState is RegisterViewModel.RegisterState.Loading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Зарегистрироваться")
        }

        if (registerState is RegisterViewModel.RegisterState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text(text = (registerState as RegisterViewModel.RegisterState.Error).message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}