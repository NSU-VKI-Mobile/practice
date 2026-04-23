package ci.nsu.moble.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MainScreen(
    onCalculate: () -> Unit,
    onHistory: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)
        ) { Text("Рассчитать") }
        Button(
            onClick = onHistory,
            modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)
        ) { Text("История расчётов") }
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)
        ) { Text("Закрыть приложение") }
    }
}

//1 этап
@Composable
fun Step1Screen(
    onBack: () -> Unit,
    onNext: (Double, Int) -> Unit
) {
    val viewModel: Step1ViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.amount,
            onValueChange = viewModel::setAmount,
            label = { Text("Стартовый взнос") },
            isError = state.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (state.amountError) Text("Введите положительное число") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.months,
            onValueChange = viewModel::setMonths,
            label = { Text("Срок вклада (мес.)") },
            isError = state.monthsError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (state.monthsError) Text("Введите целое положительное число") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("В начало") }
            Button(
                onClick = {
                    viewModel.getValidatedData()?.let { (amount, months) ->
                        onNext(amount, months)
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Далее") }
        }
    }
}

//2 этап
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    periodMonths: Int,
    onBack: () -> Unit,
    onCalculate: (Double, Double?) -> Unit
) {
    val viewModel: Step2ViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(periodMonths) { viewModel.setPeriod(periodMonths) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.showRateWarning) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    "Срок не указан или некорректен. Вернитесь и введите корректный срок.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = state.interestRate?.let { "$it %" } ?: "Выберите ставку",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !state.showRateWarning
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate %") },
                        onClick = {
                            viewModel.setRate(rate)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.monthlyTopUp,
            onValueChange = viewModel::setTopUp,
            label = { Text("Ежемесячное пополнение (необязательно)") },
            isError = state.topUpError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (state.topUpError) Text("Введите положительное число или оставьте пустым") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Назад") }
            Button(
                enabled = state.interestRate != null && !state.showRateWarning,
                onClick = {
                    viewModel.getValidatedData()?.let { (rate, topUp) ->
                        onCalculate(rate, topUp)
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Рассчитать") }
        }
    }
}


@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double?,
    onSave: () -> Unit,
    onBackToMain: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val finalAmount = remember(initialAmount, periodMonths, interestRate, monthlyTopUp) {
        var total = initialAmount
        val monthlyRate = interestRate / 100 / 12
        repeat(periodMonths) {
            total += total * monthlyRate
            monthlyTopUp?.let { total += it }
        }
        total
    }
    val interestEarned = finalAmount - initialAmount - (monthlyTopUp ?: 0.0) * periodMonths

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("Результаты расчёта", style = MaterialTheme.typography.headlineSmall)

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Стартовый взнос: ${"%.2f".format(initialAmount)}")
                Text("Срок вклада: $periodMonths мес.")
                Text("Процентная ставка: $interestRate %")
                if (monthlyTopUp != null) {
                    Text("Ежемесячное пополнение: ${"%.2f".format(monthlyTopUp)}")
                }
                HorizontalDivider()
                Text("Итоговая сумма: ${"%.2f".format(finalAmount)}", style = MaterialTheme.typography.titleMedium)
                Text("Начисленные проценты: ${"%.2f".format(interestEarned)}")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        val app = context.applicationContext as DepositApp
                        app.repository.saveCalculation(
                            DepositCalculation(
                                initialAmount = initialAmount,
                                periodMonths = periodMonths,
                                interestRate = interestRate,
                                monthlyTopUp = monthlyTopUp,
                                finalAmount = finalAmount,
                                interestEarned = interestEarned
                            )
                        )
                        onSave()
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Сохранить") }
            Button(onClick = onBackToMain, modifier = Modifier.weight(1f)) { Text("В начало") }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = viewModel(),
    onItemClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val calculations by historyViewModel.calculations.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(calculations) { calculation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onItemClick(calculation.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                .format(Date(calculation.calculationDate))
                        )
                        Text("Взнос: ${"%.2f".format(calculation.initialAmount)}")
                        Text("Итог: ${"%.2f".format(calculation.finalAmount)}")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Long,
    detailViewModel: DetailViewModel = viewModel(),
    onBack: () -> Unit
) {
    val calculation by detailViewModel.calculation.collectAsState()

    LaunchedEffect(id) { detailViewModel.loadCalculation(id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } }
            )
        }
    ) { paddingValues ->
        calculation?.let { calc ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Дата: ${
                    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(Date(calc.calculationDate))
                }")
                Text("Стартовый взнос: ${"%.2f".format(calc.initialAmount)}")
                Text("Срок: ${calc.periodMonths} мес.")
                Text("Ставка: ${calc.interestRate} %")
                calc.monthlyTopUp?.let { Text("Пополнение: ${"%.2f".format(it)}") }
                Text("Итоговая сумма: ${"%.2f".format(calc.finalAmount)}")
                Text("Начисленные проценты: ${"%.2f".format(calc.interestEarned)}")
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}