package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.model.request.PersonDto
import ci.nsu.mobile.main.data.model.request.RegisterRequest
import ci.nsu.mobile.main.entity.DepositEntity
import ci.nsu.mobile.main.ui.AppViewModelFactory
import ci.nsu.mobile.main.ui.AuthViewModel
import ci.nsu.mobile.main.ui.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        ServiceLocator.init(this)
        setContent { AppRoot() }
    }
}

@Composable
fun AppRoot() {
    val factory    = remember { AppViewModelFactory(ServiceLocator.get()) }
    val authVm: AuthViewModel    = viewModel(factory = factory)
    val depositVm: DepositViewModel = viewModel(factory = factory)
    AppNavGraph(authVm, depositVm)
}

@Composable
fun AppNavGraph(authVm: AuthViewModel, depositVm: DepositViewModel) {
    val nav   = rememberNavController()
    val start = if (TokenManager.token != null) "main" else "login"

    NavHost(navController = nav, startDestination = start) {

        composable("login") {
            LoginScreen(
                vm            = authVm,
                onLoginSuccess = {
                    nav.navigate("main") { popUpTo("login") { inclusive = true } }
                },
                onRegisterClick = { nav.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(vm = authVm, onSuccess = { nav.popBackStack() })
        }

        composable("main") {
            MainScreen(
                authVm    = authVm,
                depositVm = depositVm,
                onLogout  = {
                    authVm.logout()
                    nav.navigate("login") { popUpTo("main") { inclusive = true } }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authVm: AuthViewModel,
    depositVm: DepositViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick  = { selectedTab = 0 },
                    icon     = { Icon(Icons.Default.Person, null) },
                    label    = { Text("Пользователи") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick  = { selectedTab = 1 },
                    icon     = { Icon(Icons.Default.List, null) },
                    label    = { Text("Мои расчёты") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick  = { selectedTab = 2 },
                    icon     = { Icon(Icons.Default.Add, null) },
                    label    = { Text("Новый расчёт") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> UsersTab(authVm)
                1 -> DepositsTab(depositVm)
                2 -> NewCalculationFlow(depositVm)
            }
        }
    }
}

@Composable
fun UsersTab(vm: AuthViewModel) {
    LaunchedEffect(Unit) { vm.loadUsers() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (vm.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        }
        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.users) { user ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text("ID: ${user.id}", style = MaterialTheme.typography.labelSmall)
                        Text(user.login,        style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun DepositsTab(vm: DepositViewModel) {
    val history by vm.history.collectAsState()
    val fmt = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    if (history.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Нет сохраненных расчётов")
        }
        return
    }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(history, key = { it.id }) { item ->
            DepositCard(item, fmt, onDelete = { vm.deleteDeposit(item) })
        }
    }
}

@Composable
fun DepositCard(item: DepositEntity, fmt: SimpleDateFormat, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("${item.startAmount} ₽ → ${String.format("%.2f", item.finalAmount)} ₽",
                    style = MaterialTheme.typography.bodyLarge)
                Text("Срок: ${item.months} мес. | Ставка: ${item.rate}%",
                    style = MaterialTheme.typography.bodySmall)
                Text("Прибыль: ${String.format("%.2f", item.profit)} ₽",
                    style = MaterialTheme.typography.bodySmall)
                Text(fmt.format(Date(item.date)),
                    style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun NewCalculationFlow(vm: DepositViewModel) {
    when (vm.currentStep) {
        0 -> CalcStepOne(vm)
        1 -> CalcStepTwo(vm)
        2 -> CalcResult(vm)
    }
}

@Composable
fun CalcStepOne(vm: DepositViewModel) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Шаг 1: Параметры вклада", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = vm.startAmount,
            onValueChange = { vm.startAmount = it },
            label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = vm.months,
            onValueChange = { vm.months = it },
            label = { Text("Срок (месяцы)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { vm.goToStep(1) },
            enabled = vm.startAmount.isNotBlank() && vm.months.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Далее") }
    }
}

@Composable
fun CalcStepTwo(vm: DepositViewModel) {
    val rates = vm.resolveRate()

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Шаг 2: Дополнительно", style = MaterialTheme.typography.titleMedium)

        if (rates.isEmpty()) {
            Text("Введите корректный срок на шаге 1",
                color = MaterialTheme.colorScheme.error)
        } else {
            Text("Выберите процентную ставку:")
            rates.forEach { r ->
                OutlinedButton(
                    onClick = { vm.rate = r },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (vm.rate == r)
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    else ButtonDefaults.outlinedButtonColors()
                ) { Text("$r %") }
            }
        }

        OutlinedTextField(
            value = vm.monthlyTopUp,
            onValueChange = { vm.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (₽)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { vm.goToStep(0) },
                modifier = Modifier.weight(1f)
            ) { Text("Назад") }

            Button(
                onClick = { vm.calculateDeposit() },
                enabled = vm.rate > 0,
                modifier = Modifier.weight(1f)
            ) { Text("Рассчитать") }
        }
    }
}

@Composable
fun CalcResult(vm: DepositViewModel) {
    val res = vm.result ?: return
    val fmt = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.titleMedium)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Начальный взнос: ${res.startAmount} ₽")
                Text("Срок: ${res.months} мес.")
                Text("Ставка: ${res.rate} %")
                Text("Пополнение: ${res.monthlyTopUp ?: 0.0} ₽/мес.")
                Divider()
                Text("Итоговая сумма: ${String.format("%.2f", res.finalAmount)} ₽",
                    style = MaterialTheme.typography.bodyLarge)
                Text("Прибыль: ${String.format("%.2f", res.profit)} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary)
                Text("Дата: ${fmt.format(Date(res.date))}",
                    style = MaterialTheme.typography.labelSmall)
            }
        }

        vm.savedMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { vm.saveDeposit() },
            enabled = vm.savedMessage == null,   // нельзя сохранить дважды
            modifier = Modifier.fillMaxWidth()
        ) { Text("Сохранить") }

        OutlinedButton(
            onClick = { vm.resetCalculation() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Новый расчёт") }
    }
}

@Composable
fun LoginScreen(vm: AuthViewModel, onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    var login    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(login,    { login    = it }, label = { Text("Логин")   }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(password, { password = it }, label = { Text("Пароль")  }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { vm.login(login, password, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Войти") }
        TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
            Text("Зарегистрироваться")
        }
        if (vm.isLoading) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: AuthViewModel, onSuccess: () -> Unit) {
    var login    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var phone    by remember { mutableStateOf("") }
    var firstName  by remember { mutableStateOf("") }
    var lastName   by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var birthDate  by remember { mutableStateOf("2000-01-01") }
    var gender     by remember { mutableStateOf("MALE") }
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var groupExpanded   by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.loadGroups() }

    LazyColumn(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Регистрация", style = MaterialTheme.typography.headlineMedium) }

        item { OutlinedTextField(firstName,  { firstName  = it }, label = { Text("Имя") },      modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(lastName,   { lastName   = it }, label = { Text("Фамилия") },   modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(middleName, { middleName = it }, label = { Text("Отчество") },  modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(birthDate,  { birthDate  = it }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth()) }

        // Выбор пола
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
            ExposedDropdownMenuBox(expanded = groupExpanded, onExpandedChange = { groupExpanded = !groupExpanded }) {
                OutlinedTextField(
                    value    = vm.groups.find { it.groupId == selectedGroupId }?.groupName ?: "Выберите группу",
                    onValueChange = {},
                    readOnly = true,
                    label    = { Text("Группа") },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = groupExpanded, onDismissRequest = { groupExpanded = false }) {
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
                            login = login, password = password,
                            email = email, phoneNumber = phone,
                            roleId = 1, authAllowed = true,
                            person = PersonDto(
                                firstName  = firstName,
                                lastName   = lastName,
                                middleName = middleName,
                                birthDate  = birthDate,
                                gender     = gender,
                                groupId    = gid
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