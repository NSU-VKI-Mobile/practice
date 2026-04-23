package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.model.request.PersonDto
import ci.nsu.mobile.main.data.model.request.RegisterRequest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)

        setContent {
            val vm = remember { AuthViewModel() }
            AppNavGraph(vm = vm)
        }
    }
}
@Composable
fun AppNavGraph(vm: AuthViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = if (TokenManager.token != null) "main" else "login"
    ) {

        composable("login") {
            LoginScreen(vm = vm, onLoginSuccess = {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }, onRegisterClick = {
                navController.navigate("register")
            })
        }

        composable("register") {
            RegisterScreen(
                vm = vm, onSuccess = {
                    navController.popBackStack()
                })
        }

        composable("main") {
            MainScreen(
                vm = vm, onLogout = {
                    vm.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                })
        }
    }
}

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        TextField(login, { login = it }, label = { Text("Login") })
        TextField(password, { password = it }, label = { Text("Password") })

        Button(onClick = {
            vm.login(login, password, onLoginSuccess)
        }) {
            Text("Войти")
        }

        TextButton(onClick = onRegisterClick) {
            Text("Регистрация")
        }

        if (vm.isLoading) CircularProgressIndicator()

        vm.error?.let { Text(it) }
    }
}
@Composable
fun MainScreen(
    vm: AuthViewModel,
    onLogout: () -> Unit
) {

    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = onLogout) {
            Text("Выйти")
        }

        if (vm.isLoading) {
            CircularProgressIndicator()
        }

        LazyColumn {
            items(vm.users) {
                Text("${it.id} ${it.login}")
            }
        }
    }
}
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onSuccess: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var groupExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.loadGroups()
    }

    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(firstName, { firstName = it }, label = { Text("Имя") })
        TextField(lastName, { lastName = it }, label = { Text("Фамилия") })
        TextField(middleName, { middleName = it }, label = { Text("Отчество") })
        TextField(
            birthDate, { birthDate = it },
            label = { Text("Дата рождения (2000-01-01)") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Пол: ")
            RadioButton(selected = gender == "MALE", onClick = { gender = "MALE" })
            Text("М")
            RadioButton(selected = gender == "FEMALE", onClick = { gender = "FEMALE" })
            Text("Ж")
        }

        Box {
            OutlinedButton(onClick = { groupExpanded = true }) {
                val groupName = vm.groups.find { it.groupId == selectedGroupId }?.groupName
                Text(groupName ?: "Выберите группу")
            }
            DropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = { groupExpanded = false }
            ) {
                vm.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.groupName) },
                        onClick = {
                            selectedGroupId = group.groupId
                            groupExpanded = false
                        }
                    )
                }
            }
        }

        TextField(login, { login = it }, label = { Text("Логин") })
        TextField(password, { password = it }, label = { Text("Пароль") })
        TextField(email, { email = it }, label = { Text("Email") })
        TextField(phoneNumber, { phoneNumber = it }, label = { Text("Телефон") })

        Button(
            onClick = {
                val groupId = selectedGroupId ?: return@Button
                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phoneNumber,
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
                vm.register(request, onSuccess)
            },
            enabled = selectedGroupId != null && login.isNotBlank() && password.isNotBlank()
        ) {
            Text("Зарегистрироваться")
        }

        if (vm.isLoading) CircularProgressIndicator()
        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}