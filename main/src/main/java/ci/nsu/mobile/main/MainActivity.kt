package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    private lateinit var repository: DepositRepository
    private lateinit var historyViewModel: HistoryViewModel

    // ← ВЫНЕСИТЕ ViewModel СЮДА (живут всё время)
    private lateinit var firstViewModel: FirstStepViewModel
    private lateinit var secondViewModel: SecondStepViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = DepositRepository(this)
        historyViewModel = HistoryViewModel(repository)

        // ← ИНИЦИАЛИЗИРУЙТЕ ЗДЕСЬ
        firstViewModel = FirstStepViewModel()
        secondViewModel = SecondStepViewModel()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("main") }

                    var savedInitialAmount by remember { mutableStateOf(0.0) }
                    var savedPeriodMonths by remember { mutableStateOf(0) }
                    var savedInterestRate by remember { mutableStateOf(0.0) }
                    var savedMonthlyTopUp by remember { mutableStateOf(0.0) }

                    when (currentScreen) {
                        "main" -> {
                            MainScreen(
                                onCalculateClick = {
                                    // Очистка только при новом расчёте
                                    firstViewModel.resetData()
                                    secondViewModel.resetData()
                                    savedInitialAmount = 0.0
                                    savedPeriodMonths = 0
                                    savedInterestRate = 0.0
                                    savedMonthlyTopUp = 0.0
                                    currentScreen = "first"
                                },
                                onHistoryClick = { currentScreen = "history" },
                                onExitClick = { finish() }
                            )
                        }
                        "first" -> {
                            // ИСПОЛЬЗУЕМ ТОТ ЖЕ САМЫЙ ViewModel (не создаём новый)
                            FirstStepScreen(
                                viewModel = firstViewModel,
                                onBackClick = {
                                    currentScreen = "main"
                                },
                                onNextClick = { amount, months ->
                                    savedInitialAmount = amount
                                    savedPeriodMonths = months
                                    currentScreen = "second"
                                }
                            )
                        }
                        "second" -> {
                            // ИСПОЛЬЗУЕМ ТОТ ЖЕ САМЫЙ ViewModel
                            SecondStepScreen(
                                initialAmount = savedInitialAmount,
                                periodMonths = savedPeriodMonths,
                                viewModel = secondViewModel,
                                onBackClick = {
                                    // НЕ ОЧИЩАЕМ - просто возврат
                                    currentScreen = "first"
                                },
                                onCalculateClick = { amount, months, rate, topUp ->
                                    savedInitialAmount = amount
                                    savedPeriodMonths = months
                                    savedInterestRate = rate
                                    savedMonthlyTopUp = topUp
                                    currentScreen = "result"
                                }
                            )
                        }
                        "result" -> {
                            val resultViewModel = remember { ResultViewModel() }
                            ResultScreen(
                                initialAmount = savedInitialAmount,
                                periodMonths = savedPeriodMonths,
                                interestRate = savedInterestRate,
                                monthlyTopUp = savedMonthlyTopUp,
                                viewModel = resultViewModel,
                                onSaveClick = { calculation ->
                                    historyViewModel.addCalculation(calculation)
                                },
                                onBackToMainClick = {
                                    currentScreen = "main"
                                }
                            )
                        }
                        "history" -> {
                            HistoryScreen(
                                viewModel = historyViewModel,
                                onBackClick = { currentScreen = "main" }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Расчёт вкладов", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = onCalculateClick, modifier = Modifier.fillMaxWidth()) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth()) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExitClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Закрыть приложение")
        }
    }
}


@Composable
fun FirstStepScreen(
    viewModel: FirstStepViewModel,
    onBackClick: () -> Unit,
    onNextClick: (Double, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Параметры вклада", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.initialAmount,
            onValueChange = { viewModel.updateInitialAmount(it) },
            label = { Text("Стартовый взнос") },
            isError = viewModel.initialAmountError != null,
            supportingText = { if (viewModel.initialAmountError != null) Text(viewModel.initialAmountError!!) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.periodMonths,
            onValueChange = { viewModel.updatePeriodMonths(it) },
            label = { Text("Срок вклада (месяцы)") },
            isError = viewModel.periodError != null,
            supportingText = { if (viewModel.periodError != null) Text(viewModel.periodError!!) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onBackClick, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text("В начало")
            }
            Button(
                onClick = {
                    val (amount, months) = viewModel.getData()
                    onNextClick(amount, months)
                },
                enabled = viewModel.isNextEnabled,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Далее")
            }
        }
    }
}


@Composable
fun SecondStepScreen(
    initialAmount: Double,
    periodMonths: Int,
    viewModel: SecondStepViewModel,
    onBackClick: () -> Unit,
    onCalculateClick: (Double, Int, Double, Double) -> Unit
) {
    LaunchedEffect(periodMonths) {
        viewModel.updatePeriodMonths(periodMonths)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Дополнительные параметры", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Процентная ставка")

        if (viewModel.availableRates.isNotEmpty()) {
            Row {
                viewModel.availableRates.forEach { rate ->
                    FilterChip(
                        selected = viewModel.selectedRate == rate,
                        onClick = { viewModel.selectRate(rate) },
                        label = { Text("$rate%") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.updateMonthlyTopUp(it) },
            label = { Text("Ежемесячное пополнение (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorMessage != null) {
            Text(viewModel.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onBackClick, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text("Назад")
            }
            Button(
                onClick = {
                    val (rate, topUp) = viewModel.getData()
                    onCalculateClick(initialAmount, periodMonths, rate, topUp)
                },
                enabled = viewModel.isCalculateEnabled,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Рассчитать")
            }
        }
    }
}


@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double,
    viewModel: ResultViewModel,
    onSaveClick: (DepositEntity) -> Unit,
    onBackToMainClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.calculate(initialAmount, periodMonths, interestRate, monthlyTopUp)
    }

    var showSavedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Результат расчёта", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${String.format("%.2f", viewModel.initialAmount)} ₽")
                Text("Срок: ${viewModel.periodMonths} месяцев")
                Text("Процентная ставка: ${viewModel.interestRate}%")
                if (viewModel.monthlyTopUp > 0) {
                    Text("Ежемесячное пополнение: ${String.format("%.2f", viewModel.monthlyTopUp)} ₽")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Text("Итоговая сумма: ${String.format("%.2f", viewModel.finalAmount)} ₽")
                Text("Начисленные проценты: ${String.format("%.2f", viewModel.interestEarned)} ₽")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (viewModel.canSave()) {
                        onSaveClick(viewModel.getDepositEntity())
                        showSavedMessage = true
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Сохранить")
            }
            Button(
                onClick = onBackToMainClick,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("В начало")
            }
        }

        if (showSavedMessage) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = " Расчёт сохранён!",
                color = Color.Green,
                fontSize = 14.sp
            )
        }
    }
}


@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit
) {
    val calculations by viewModel.calculations.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Text("История расчётов", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = calculations,
                    key = { it.id }
                ) { calc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(formatDate(calc.calculationDate), fontSize = 12.sp)
                            Text("Стартовый взнос: ${String.format("%.2f", calc.initialAmount)} ₽")
                            Text("Итоговая сумма: ${String.format("%.2f", calc.finalAmount)} ₽")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isNotEmpty()) {
            Button(
                onClick = { viewModel.clearHistory() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Очистить всю историю")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("На главную")
        }
    }
}

// Добавьте эту функцию в конец файла, если её нет
fun formatDate(timestamp: Long): String {
    val format = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
    return format.format(java.util.Date(timestamp))
}