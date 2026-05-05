package ci.nsu.mobile.main.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDateInput by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf<Int?>(null) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    val genderOptions = listOf("MALE", "FEMALE")
    fun convertDate(input: String): String {
        val parts = input.split(".")
        if (parts.size == 3) {
            val day = parts[0].padStart(2, '0')
            val month = parts[1].padStart(2, '0')
            val year = parts[2]
            return "$year-$month-$day"
        }
        return input
    }
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            viewModel.resetSuccess()
        }
    }
    LaunchedEffect(state.error) {
        state.error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = birthDateInput,
            onValueChange = { birthDateInput = it },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            placeholder = { Text("31.01.2000") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        ExposedDropdownMenuBox(
            expanded = expandedGender,
            onExpandedChange = { expandedGender = it }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false }
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            gender = option
                            expandedGender = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


        ExposedDropdownMenuBox(
            expanded = expandedGroup,
            onExpandedChange = { expandedGroup = it }
        ) {
            OutlinedTextField(
                value = state.groups.find { it.id == groupId }?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGroup,
                onDismissRequest = { expandedGroup = false }
            ) {
                state.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            groupId = group.id
                            expandedGroup = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val finalDate = convertDate(birthDateInput)
                val person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName.ifBlank { "" },
                    birthDate = finalDate,
                    gender = gender,
                    groupId = groupId ?: 0
                )
                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phone,
                    person = person
                )
                viewModel.register(request)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && groupId != null && gender.isNotEmpty()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Зарегистрироваться")
            }
        }

        if (state.isLoadingGroups) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
        state.groupsError?.let {
            Text("Ошибка загрузки групп: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}