package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.models.DepositCalculation
import ci.nsu.mobile.main.ui.viewmodel.MainViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    depositViewModel: DepositViewModel
) {
    // Состояние выбранной вкладки (0 - Пользователи, 1 - Новый расчет, 2 - История)
    var selectedTab by remember { mutableStateOf(0) }
    val uiState by mainViewModel.uiState.collectAsState()

    // Загружаем пользователей только раз при старте
    LaunchedEffect(Unit) {
        mainViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "Пользователи"
                            1 -> "Новый расчёт"
                            else -> "История расчётов"
                        }
                    )
                },
                actions = {
                    Button(
                        onClick = {
                            mainViewModel.logout()
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Юзеры") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Калькулятор") },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("История") },
                    icon = { Icon(Icons.Default.List, contentDescription = null) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> UsersListTab(uiState = uiState, onRetry = { mainViewModel.loadUsers() })
                1 -> NewCalculationTab(depositViewModel = depositViewModel)
                2 -> HistoryTab(depositViewModel = depositViewModel)
            }
        }
    }
}

// --- ВКЛАДКА 1: СПИСОК ПОЛЬЗОВАТЕЛЕЙ (Твой оригинальный код) ---
@Composable
fun UsersListTab(uiState: ci.nsu.mobile.main.ui.viewmodel.MainUiState, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) { Text("Повторить") }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users) { user ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = user.login, style = MaterialTheme.typography.titleMedium)
                                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                                Text(text = "ID пользователя: ${user.id}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Последний вход: ${user.lastLoginDate ?: "никогда"}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- ВКЛАДКА 2: ФОРМА ДЛЯ НОВОГО РАСЧЕТА ДЕПОЗИТА ---
@Composable
fun NewCalculationTab(depositViewModel: DepositViewModel) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var topUp by remember { mutableStateOf("") }

    var resultText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Сумма вклада (руб.)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = months,
            onValueChange = { months = it },
            label = { Text("Срок (месяцев)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Процентная ставка (% годовых)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = topUp,
            onValueChange = { topUp = it },
            label = { Text("Ежемесячное пополнение (руб., опционально)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val startAmount = amount.toDoubleOrNull() ?: 0.0
                val period = months.toIntOrNull() ?: 0
                val percent = rate.toDoubleOrNull() ?: 0.0
                val monthly = topUp.toDoubleOrNull() ?: 0.0

                if (startAmount > 0 && period > 0 && percent > 0) {
                    // Простейший расчет доходности для лабы
                    val interestEarned = startAmount * (percent / 100) * (period / 12.0)
                    val finalAmount = startAmount + interestEarned + (monthly * period)

                    depositViewModel.saveCalculation(
                        initialAmount = startAmount,
                        periodMonths = period,
                        interestRate = percent,
                        monthlyTopUp = if (monthly > 0) monthly else null,
                        finalAmount = finalAmount,
                        interestEarned = interestEarned
                    )
                    resultText = "Расчёт сохранён! Итоговая сумма: %.2f руб.".format(finalAmount)
                } else {
                    resultText = "Заполните поля корректными числами"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать и сохранить")
        }

        if (resultText.isNotEmpty()) {
            Text(text = resultText, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// --- ВКЛАДКА 3: ИСТОРИЯ РАСЧЕТОВ ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ ---
@Composable
fun HistoryTab(depositViewModel: DepositViewModel) {
    val history by depositViewModel.historyState.collectAsState()

    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("История расчётов пуста")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(history) { calc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Вклад: ${calc.initialAmount} руб.", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Срок: ${calc.periodMonths} мес. под ${calc.interestRate}%", style = MaterialTheme.typography.bodyMedium)
                        if (calc.monthlyTopUp != null) {
                            Text(text = "Пополнение: ${calc.monthlyTopUp} руб./мес.", style = MaterialTheme.typography.bodySmall)
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Text(text = "Доход: %.2f руб.".format(calc.interestEarned), color = MaterialTheme.colorScheme.primary)
                        Text(text = "Итог: %.2f руб.".format(calc.finalAmount), style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }
    }
}