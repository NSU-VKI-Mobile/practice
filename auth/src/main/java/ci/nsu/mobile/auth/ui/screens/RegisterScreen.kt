package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.auth.data.model.request.PersonDto
import ci.nsu.mobile.auth.data.model.request.RegisterRequest
import ci.nsu.mobile.auth.ui.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: AuthViewModel, onSuccess: () -> Unit) {
    var login         by remember { mutableStateOf("") }
    var password      by remember { mutableStateOf("") }
    var email         by remember { mutableStateOf("") }
    var phone         by remember { mutableStateOf("") }
    var firstName     by remember { mutableStateOf("") }
    var lastName      by remember { mutableStateOf("") }
    var middleName    by remember { mutableStateOf("") }
    var birthDate     by remember { mutableStateOf("2000-01-01") }
    var gender        by remember { mutableStateOf("MALE") }
    var selectedGroupId  by remember { mutableStateOf<Int?>(null) }
    var groupExpanded    by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.loadGroups() }

    LazyColumn(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Регистрация", style = MaterialTheme.typography.headlineMedium) }

        item { OutlinedTextField(firstName,  { firstName  = it }, label = { Text("Имя") },        modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(lastName,   { lastName   = it }, label = { Text("Фамилия") },     modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(middleName, { middleName = it }, label = { Text("Отчество") },    modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(birthDate,  { birthDate  = it }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth()) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("MALE" to "Мужской", "FEMALE" to "Женский").forEach { (value, label) ->
                    FilterChip(
                        selected = gender == value,
                        onClick  = { gender = value },
                        label    = { Text(label) }
                    )
                }
            }
        }

        item {
            ExposedDropdownMenuBox(
                expanded        = groupExpanded,
                onExpandedChange = { groupExpanded = !groupExpanded }
            ) {
                OutlinedTextField(
                    value         = vm.groups.find { it.groupId == selectedGroupId }?.groupName ?: "Выберите группу",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Группа") },
                    modifier      = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded        = groupExpanded,
                    onDismissRequest = { groupExpanded = false }
                ) {
                    vm.groups.forEach { group ->
                        DropdownMenuItem(
                            text    = { Text(group.groupName) },
                            onClick = { selectedGroupId = group.groupId; groupExpanded = false }
                        )
                    }
                }
            }
        }

        item { OutlinedTextField(login,    { login    = it }, label = { Text("Логин") },   modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(password, { password = it }, label = { Text("Пароль") },  modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(email,    { email    = it }, label = { Text("Email") },   modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(phone,    { phone    = it }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth()) }

        item {
            Button(
                onClick = {
                    val gid = selectedGroupId ?: return@Button
                    vm.register(
                        RegisterRequest(
                            login       = login, password    = password,
                            email       = email, phoneNumber = phone,
                            roleId      = 1,     authAllowed = true,
                            person      = PersonDto(
                                firstName  = firstName,  lastName   = lastName,
                                middleName = middleName, birthDate  = birthDate,
                                gender     = gender,     groupId    = gid
                            )
                        ),
                        onSuccess = onSuccess
                    )
                },
                enabled  = selectedGroupId != null && login.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Зарегистрироваться") }
        }

        item {
            if (vm.isLoading) CircularProgressIndicator()
            vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}