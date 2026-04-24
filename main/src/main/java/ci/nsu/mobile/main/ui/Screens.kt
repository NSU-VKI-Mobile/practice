package ci.nsu.mobile.main.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.PersonDto
import ci.nsu.mobile.main.data.RegisterRequest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(viewModel.isUserLoggedIn) {
        if (viewModel.isUserLoggedIn) {
            navController.navigate("main_flow") { popUpTo("login") { inclusive = true } }
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
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) CircularProgressIndicator()
            else {
                Button(onClick = { viewModel.login(login, password) }, modifier = Modifier.fillMaxWidth()) { Text("Войти") }
                TextButton(onClick = { navController.navigate("register") }) { Text("Нет аккаунта? Зарегистрироваться") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }; var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }; var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }; var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var selectedGroupName by remember { mutableStateOf("Выберите группу") }

    LaunchedEffect(Unit) { viewModel.loadGroups() }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show(); viewModel.errorMessage = null }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Регистрация") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(scrollState)) {
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = middleName, onValueChange = { middleName = it }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Пол (MALE/FEMALE)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(value = selectedGroupName, onValueChange = {}, readOnly = true, label = { Text("Группа") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    viewModel.groupsList.forEach { group ->
                        DropdownMenuItem(text = { Text(group.name) }, onClick = { selectedGroupId = group.id; selectedGroupName = group.name; expanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            else {
                Button(onClick = {
                    if (selectedGroupId == null || login.isBlank() || password.isBlank() || firstName.isBlank()) {
                        Toast.makeText(context, "Заполните основные поля", Toast.LENGTH_SHORT).show(); return@Button
                    }
                    val request = RegisterRequest(login, password, email, phone, roleId = 1, authAllowed = true, person = PersonDto(firstName, lastName, middleName.ifBlank { null }, birthDate, gender, selectedGroupId!!))
                    viewModel.register(request) { Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show(); navController.popBackStack() }
                }, modifier = Modifier.fillMaxWidth()) { Text("Зарегистрироваться") }
            }
        }
    }
}

@Composable
fun UsersScreen(viewModel: AuthViewModel) {
    LaunchedEffect(Unit) { viewModel.loadUsers() }
    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.usersList) { user ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Логин: ${user.login}", style = MaterialTheme.typography.titleMedium)
                            Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                            user.phoneNumber?.let { Text("Телефон: $it", style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(viewModel: DepositViewModel) {
    val historyList by viewModel.history.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(historyList) { item ->
                var isExpanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { isExpanded = !isExpanded }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Дата: ${dateFormat.format(Date(item.calculationDate))}", style = MaterialTheme.typography.labelSmall)
                        Text("Стартовый взнос: ${item.initialAmount}")
                        Text("Итог: ${String.format(Locale.US, "%.2f", item.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                        if (isExpanded) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Срок вклада: ${item.periodMonths} мес.")
                            Text("Ставка: ${item.interestRate}%")
                            Text("Пополнение: ${item.monthlyTopUp}/мес.")
                            Text("Начисленные проценты: ${String.format(Locale.US, "%.2f", item.interestEarned)}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepOneScreen(navController: NavController, viewModel: DepositViewModel) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.initialAmount, onValueChange = { viewModel.initialAmount = it }, label = { Text("Стартовый взнос") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.periodMonths, onValueChange = { viewModel.periodMonths = it }, label = { Text("Срок вклада (в месяцах)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if (viewModel.initialAmount.isBlank() || viewModel.periodMonths.isBlank()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.interestRate = viewModel.determineInterestRate()
                navController.navigate("step_two")
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Далее") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(navController: NavController, viewModel: DepositViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currentRate = viewModel.interestRate.toString()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Шаг 2: Доп. параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(value = "$currentRate%", onValueChange = {}, readOnly = true, label = { Text("Доступная процентная ставка") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("$currentRate%") }, onClick = { expanded = false })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.monthlyTopUp, onValueChange = { viewModel.monthlyTopUp = it }, label = { Text("Ежемесячное пополнение") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Назад") }
            Button(onClick = {
                viewModel.monthlyTopUp = viewModel.monthlyTopUp.ifBlank { "0" }
                viewModel.calculateResult()
                navController.navigate("result")
            }) { Text("Рассчитать") }
        }
    }
}

@Composable
fun ResultScreen(navController: NavController, viewModel: DepositViewModel) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Результат", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${viewModel.initialAmount}")
                Text("Срок: ${viewModel.periodMonths} мес.")
                Text("Ставка: ${viewModel.interestRate}%")
                Text("Пополнение: ${viewModel.monthlyTopUp.ifBlank { "0" }}/мес.")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Начисленные проценты: ${String.format("%.2f", viewModel.interestEarned)}")
                Text("Итоговая сумма: ${String.format("%.2f", viewModel.finalAmount)}", style = MaterialTheme.typography.titleLarge)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                viewModel.saveCalculation()
                Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
                viewModel.clearData()
                navController.popBackStack("step_one", inclusive = false)
            }) { Text("Сохранить") }
            OutlinedButton(onClick = {
                viewModel.clearData()
                navController.popBackStack("step_one", inclusive = false)
            }) { Text("Сбросить") }
        }
    }
}