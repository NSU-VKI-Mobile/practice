package ci.nsu.mobile.main.ui.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") },
                actions = {
                    TextButton(onClick = { viewModel.logout(onLogout) }) {
                        Text("Выйти", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {},
                    label = { Text("Пользователи") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {},
                    label = { Text("Мои расчёты") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {},
                    label = { Text("Новый расчёт") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> UsersTab(uiState = uiState)
                1 -> CalculationsTab(
                    uiState = uiState,
                    onDelete = { id -> viewModel.deleteCalculation(id) }
                )
                2 -> NewCalculationTab(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun UsersTab(uiState: MainUiState) {
    if (uiState.isLoadingUsers) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Логин: ${user.login}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Email: ${user.email}")
                        user.person?.let { person ->
                            Text("Имя: ${person.firstName} ${person.lastName}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculationsTab(
    uiState: MainUiState,
    onDelete: (Long) -> Unit
) {
    if (uiState.calculations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет сохранённых расчётов")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.calculations) { calc ->
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = dateFormat.format(Date(calc.calculationDate)),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text("Взнос: ${calc.initialAmount} ₽")
                        Text("Срок: ${calc.periodMonths} мес.")
                        Text("Ставка: ${calc.interestRate}%")
                        Text(
                            text = "Итог: ${"%.2f".format(calc.finalAmount)} ₽",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Доход: ${"%.2f".format(calc.interestEarned)} ₽",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onDelete(calc.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewCalculationTab(viewModel: MainViewModel) {
    val step1Data by viewModel.step1Data.collectAsState()
    val currentCalculation by viewModel.currentCalculation.collectAsState()

    var step by remember { mutableIntStateOf(1) }
    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var monthlyTopUp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        when (step) {
            1 -> {
                Text(
                    text = "Шаг 1: Основные параметры",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = initialAmount,
                    onValueChange = { initialAmount = it },
                    label = { Text("Начальная сумма (₽)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = periodMonths,
                    onValueChange = { periodMonths = it },
                    label = { Text("Срок (месяцев)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amount = initialAmount.toDoubleOrNull()
                        val months = periodMonths.toIntOrNull()
                        when {
                            amount == null || amount <= 0 ->
                                errorMessage = "Введите корректную сумму"
                            months == null || months <= 0 ->
                                errorMessage = "Введите корректный срок"
                            else -> {
                                errorMessage = null
                                viewModel.setStep1Data(amount, months)
                                step = 2
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Далее")
                }
            }

            2 -> {
                val (amount, months) = step1Data!!
                val rate = when {
                    months < 6 -> 15.0
                    months < 12 -> 10.0
                    else -> 5.0
                }

                Text(
                    text = "Шаг 2: Дополнительные параметры",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Сумма: $amount ₽")
                        Text("Срок: $months месяцев")
                        Text(
                            text = "Процентная ставка: $rate%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = monthlyTopUp,
                    onValueChange = { monthlyTopUp = it },
                    label = { Text("Ежемесячное пополнение (₽, необязательно)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val topUp = monthlyTopUp.toDoubleOrNull()
                        viewModel.calculate(amount, months, rate, topUp)
                        step = 3
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Рассчитать")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { step = 1 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад")
                }
            }

            3 -> {
                currentCalculation?.let { calc ->
                    Text(
                        text = "Результат расчёта",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Начальная сумма: ${calc.initialAmount} ₽")
                            Text("Срок: ${calc.periodMonths} месяцев")
                            Text("Ставка: ${calc.interestRate}%")
                            calc.monthlyTopUp?.let {
                                Text("Пополнение: $it ₽/мес.")
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "Итоговая сумма: ${"%.2f".format(calc.finalAmount)} ₽",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Доход: ${"%.2f".format(calc.interestEarned)} ₽",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (savedMessage != null) {
                        Text(
                            text = savedMessage!!,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            viewModel.saveCurrentCalculation {
                                savedMessage = "Расчёт сохранён!"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сохранить")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            step = 1
                            initialAmount = ""
                            periodMonths = ""
                            monthlyTopUp = ""
                            savedMessage = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Новый расчёт")
                    }
                }
            }
        }
    }
}