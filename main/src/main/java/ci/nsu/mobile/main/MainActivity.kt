package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем хранилище токена при старте приложения
        TokenManager.init(applicationContext)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthApp()
                }
            }
        }
    }
}

@Composable
fun AuthApp(viewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Если есть ошибка, показываем Toast и очищаем её
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Автоматический переход на главный экран, если авторизованы
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true } // Убираем экран логина из истории
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (uiState.isAuthenticated) "main" else "login"
    ) {

        // ================= ЭКРАН ВХОДА =================
        composable("login") {
            var login by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Вход в систему", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(24.dp))

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
                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator() // Стандартный прогресс-бар
                } else {
                    Button(
                        onClick = { viewModel.login(login, password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = login.isNotBlank() && password.isNotBlank()
                    ) {
                        Text("Войти")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("register") }) {
                        Text("Нет аккаунта? Зарегистрироваться")
                    }
                }
            }
        }

        // ================= ЭКРАН РЕГИСТРАЦИИ =================
        composable("register") {
            // Подгружаем группы при открытии экрана
            LaunchedEffect(Unit) { viewModel.fetchGroups() }

            // Локальные состояния для большой формы
            var firstName by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            var middleName by remember { mutableStateOf("") }
            var birthDate by remember { mutableStateOf("") }
            var gender by remember { mutableStateOf("M") }
            var login by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }

            var selectedGroupId by remember { mutableStateOf<Int?>(null) }
            var groupDropdownExpanded by remember { mutableStateOf(false) }

            // scrollState позволяет скроллить форму, если она не влезает на экран
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Пол (M/F)") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(8.dp))

                // Стандартный выбор группы
                Box {
                    OutlinedButton(onClick = { groupDropdownExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        val groupName = uiState.groups.find { it.id == selectedGroupId }?.name ?: "Выберите группу"
                        Text(groupName)
                    }
                    DropdownMenu(expanded = groupDropdownExpanded, onDismissRequest = { groupDropdownExpanded = false }) {
                        uiState.groups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group.name) },
                                onClick = {
                                    selectedGroupId = group.id
                                    groupDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            if (selectedGroupId != null) {
                                val request = RegisterRequest(
                                    login = login, password = password, email = email, phoneNumber = phone,
                                    person = PersonDto(firstName, lastName, middleName, birthDate, gender, selectedGroupId!!)
                                )
                                // После успешной регистрации возвращаемся на экран входа
                                viewModel.register(request) { navController.popBackStack() }
                            } else {
                                Toast.makeText(context, "Выберите группу!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        // Валидация: кнопка активна, только если все важные поля заполнены
                        enabled = login.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && selectedGroupId != null
                    ) { Text("Зарегистрироваться") }

                    TextButton(onClick = { navController.popBackStack() }) { Text("Назад ко входу") }
                }
            }
        }

        // ================= ГЛАВНЫЙ ЭКРАН (СПИСОК) =================
        composable("main") {
            LaunchedEffect(Unit) { viewModel.fetchUsers() }

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Пользователи", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = {
                        viewModel.logout()
                        navController.navigate("login") { popUpTo(0) }
                    }) { Text("Выйти") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.users.isEmpty()) {
                    Text("Список пользователей пуст")
                } else {
                    LazyColumn {
                        items(uiState.users) { user ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Логин: ${user.login}", style = MaterialTheme.typography.titleMedium)
                                    user.email?.let { Text("Email: $it", style = MaterialTheme.typography.bodyMedium) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}