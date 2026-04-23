package ci.nsu.mobile.main.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.PersonDto
import ci.nsu.mobile.main.data.RegisterRequest

@Composable
fun AppNavigation(viewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()

    val startDest = if (viewModel.isUserLoggedIn) "main" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(navController, viewModel) }
        composable("register") { RegisterScreen(navController, viewModel) }
        composable("main") { MainScreen(navController, viewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(viewModel.isUserLoggedIn) {
        if (viewModel.isUserLoggedIn) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage = null
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Вход в систему") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(login, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Нет аккаунта? Зарегистрироваться")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var selectedGroupName by remember { mutableStateOf("Выберите группу") }

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage = null
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Регистрация") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество (необязательно)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (например: 2000-01-01)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Пол (MALE/FEMALE)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedGroupName,
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
                    viewModel.groupsList.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupId = group.id
                                selectedGroupName = group.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        if (selectedGroupId == null || login.isBlank() || password.isBlank() || firstName.isBlank()) {
                            Toast.makeText(context, "Заполните основные поля и выберите группу", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val person = PersonDto(firstName, lastName, middleName.ifBlank { null }, birthDate, gender, selectedGroupId!!)
                        val request = RegisterRequest(login, password, email, phone, roleId = 1, authAllowed = true, person = person)

                        viewModel.register(request) {
                            Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: AuthViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список пользователей") },
                actions = {
                    TextButton(onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }) {
                        Text("Выйти", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.usersList) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Логин: ${user.login}", style = MaterialTheme.typography.titleMedium)
                                Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                                if (user.phoneNumber != null) {
                                    Text("Телефон: ${user.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                                }
                                Text("Person ID: ${user.personId}", style = MaterialTheme.typography.bodySmall)
                                Text("Роль ID: ${user.roleId}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}