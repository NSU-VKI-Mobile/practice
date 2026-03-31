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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Locale

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Ожидаем на вход только цифры (до 10 штук)
        val digits = text.text
        var out = "+7"
        if (digits.isNotEmpty()) out += "(${digits.take(3)}"
        if (digits.length > 3) out += ")-${digits.substring(3, minOf(6, digits.length))}"
        if (digits.length > 6) out += "-${digits.substring(6, minOf(8, digits.length))}"
        if (digits.length > 8) out += "-${digits.substring(8, minOf(10, digits.length))}"

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return 2
                if (offset <= 3) return offset + 3
                if (offset <= 6) return offset + 5
                if (offset <= 8) return offset + 6
                if (offset <= 10) return offset + 7
                return 17
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return 0
                if (offset <= 6) return offset - 3
                if (offset <= 11) return offset - 5
                if (offset <= 14) return offset - 6
                if (offset <= 17) return offset - 7
                return 10
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
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
            LaunchedEffect(Unit) { viewModel.fetchGroups() }
            val context = LocalContext.current

            var firstName by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            var middleName by remember { mutableStateOf("") }
            var birthDate by remember { mutableStateOf("") }

            // Состояние для пола
            var gender by remember { mutableStateOf("") } // Для сервера: "M" или "F"
            var genderDisplay by remember { mutableStateOf("Выберите пол") }
            var genderDropdownExpanded by remember { mutableStateOf(false) }

            var login by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") } // Храним ТОЛЬКО цифры (максимум 10)

            var selectedGroupId by remember { mutableStateOf<Int?>(null) }
            var groupDropdownExpanded by remember { mutableStateOf(false) }

            // Настраиваем классический системный календарь Android
            val calendar = java.util.Calendar.getInstance()
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    // Форматируем с нулями впереди (например 2001-05-09)
                    val formattedMonth = String.format(Locale.getDefault(), "%02d", month + 1)
                    val formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth)
                    birthDate = "$year-$formattedMonth-$formattedDay"
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )

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

                // Email с проверкой на "собачку"
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = email.isNotEmpty() && !email.contains("@"),
                    modifier = Modifier.fillMaxWidth()
                )

                // Телефон с маской
                OutlinedTextField(
                    value = phone,
                    onValueChange = { newValue ->
                        // Оставляем только цифры и ограничиваем до 10 штук
                        val digitsOnly = newValue.filter { it.isDigit() }.take(10)
                        phone = digitsOnly
                    },
                    label = { Text("Телефон") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    visualTransformation = PhoneVisualTransformation(), // Применяем нашу магию отрисовки
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(8.dp))

                // Кнопка вызова Календаря
                OutlinedButton(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall // Делаем углы как у TextField
                ) {
                    Text(if (birthDate.isEmpty()) "Нажмите, чтобы выбрать дату рождения" else "Дата рождения: $birthDate")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Выпадающий список: Пол
                Box {
                    OutlinedButton(
                        onClick = { genderDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(genderDisplay)
                    }
                    DropdownMenu(expanded = genderDropdownExpanded, onDismissRequest = { genderDropdownExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Мужской") },
                            onClick = { gender = "M"; genderDisplay = "Мужской"; genderDropdownExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Женский") },
                            onClick = { gender = "F"; genderDisplay = "Женский"; genderDropdownExpanded = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Выпадающий список: Группа
                Box {
                    OutlinedButton(
                        onClick = { groupDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
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
                            // Формируем телефон для сервера, приклеивая +7, если цифры есть
                            val finalPhone = if (phone.isNotEmpty()) "+7$phone" else ""

                            val request = RegisterRequest(
                                login = login, password = password, email = email, phoneNumber = finalPhone,
                                person = PersonDto(firstName, lastName, middleName, birthDate, gender, selectedGroupId!!)
                            )
                            viewModel.register(request) { navController.popBackStack() }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = login.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && selectedGroupId != null && gender.isNotBlank() && birthDate.isNotBlank()
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