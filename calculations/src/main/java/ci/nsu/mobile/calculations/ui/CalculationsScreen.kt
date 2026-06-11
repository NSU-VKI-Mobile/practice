package ci.nsu.mobile.calculations.ui

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

@Composable
fun CalculationsScreen(viewModel: CalculationsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Мои расчёты") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Новый расчёт") })
        }
        when (selectedTab) {
            0 -> MyCalculationsTab(uiState = uiState, onDelete = { viewModel.deleteCalculation(it) })
            1 -> NewCalculationTab(viewModel = viewModel)
        }
    }
}

@Composable
fun MyCalculationsTab(
    uiState: CalculationsUiState,
    onDelete: (Long) -> Unit
) {
    if (uiState.calculations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Нет сохранённых расчётов")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(uiState.calculations) { calc ->
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(dateFormat.format(Date(calc.calculationDate)), style = MaterialTheme.typography.labelSmall)
                        Text("Взнос: ${calc.initialAmount} ₽")
                        Text("Срок: ${calc.periodMonths} мес.")
                        Text("Ставка: ${calc.interestRate}%")
                        Text("Итог: ${"%.2f".format(calc.finalAmount)} ₽", style = MaterialTheme.typography.titleMedium)
                        Text("Доход: ${"%.2f".format(calc.interestEarned)} ₽", color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = { onDelete(calc.id) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewCalculationTab(viewModel: CalculationsViewModel) {
    val step1Data by viewModel.step1Data.collectAsState()
    val currentCalculation by viewModel.currentCalculation.collectAsState()
    var step by remember { mutableIntStateOf(1) }
    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var monthlyTopUp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        when (step) {
            1 -> {
                Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = initialAmount, onValueChange = { initialAmount = it }, label = { Text("Начальная сумма (₽)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = periodMonths, onValueChange = { periodMonths = it }, label = { Text("Срок (месяцев)") }, modifier = Modifier.fillMaxWidth())
                if (errorMessage != null) { Text(errorMessage!!, color = MaterialTheme.colorScheme.error) }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val amount = initialAmount.toDoubleOrNull()
                    val months = periodMonths.toIntOrNull()
                    when {
                        amount == null || amount <= 0 -> errorMessage = "Введите корректную сумму"
                        months == null || months <= 0 -> errorMessage = "Введите корректный срок"
                        else -> { errorMessage = null; viewModel.setStep1Data(amount, months); step = 2 }
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("Далее") }
            }
            2 -> {
                val (amount, months) = step1Data!!
                val rate = when { months < 6 -> 15.0; months < 12 -> 10.0; else -> 5.0 }
                Text("Шаг 2: Дополнительные параметры", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Сумма: $amount ₽"); Text("Срок: $months месяцев")
                        Text("Ставка: $rate%", color = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = monthlyTopUp, onValueChange = { monthlyTopUp = it }, label = { Text("Пополнение (₽, необязательно)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.calculate(amount, months, rate, monthlyTopUp.toDoubleOrNull()); step = 3 }, modifier = Modifier.fillMaxWidth()) { Text("Рассчитать") }
                OutlinedButton(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth()) { Text("Назад") }
            }
            3 -> {
                currentCalculation?.let { calc ->
                    Text("Результат расчёта", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Начальная сумма: ${calc.initialAmount} ₽")
                            Text("Срок: ${calc.periodMonths} месяцев")
                            Text("Ставка: ${calc.interestRate}%")
                            calc.monthlyTopUp?.let { Text("Пополнение: $it ₽/мес.") }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Итог: ${"%.2f".format(calc.finalAmount)} ₽", style = MaterialTheme.typography.titleLarge)
                            Text("Доход: ${"%.2f".format(calc.interestEarned)} ₽", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (savedMessage != null) Text(savedMessage!!, color = MaterialTheme.colorScheme.primary)
                    Button(onClick = { viewModel.saveCurrentCalculation { savedMessage = "Расчёт сохранён!" } }, modifier = Modifier.fillMaxWidth()) { Text("Сохранить") }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { step = 1; initialAmount = ""; periodMonths = ""; monthlyTopUp = ""; savedMessage = null }, modifier = Modifier.fillMaxWidth()) { Text("Новый расчёт") }
                }
            }
        }
    }
}