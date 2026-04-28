@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

// Добавляем OptIn для экспериментальных API Material3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositApp(
    viewModel: DepositViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentScreen) {
        is Screen.Home -> HomeScreen(viewModel)
        is Screen.Step1 -> Step1Screen(viewModel)
        is Screen.Step2 -> Step2Screen(viewModel)
        is Screen.Result -> ResultScreen(viewModel)
        is Screen.History -> HistoryScreen(viewModel)
        is Screen.Detail -> DetailScreen(viewModel)
    }
}

// Главный экран
@Composable
fun HomeScreen(viewModel: DepositViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = { viewModel.navigateTo(Screen.Step1) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Рассчитать", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.navigateTo(Screen.History) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("История расчётов", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        Button(
            onClick = { (context as? Activity)?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Закрыть приложение", fontSize = 18.sp)
        }
    }
}

// Этап 1: Ввод основных параметров
@Composable
fun Step1Screen(viewModel: DepositViewModel) {
    val inputState by viewModel.inputState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Параметры вклада") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = inputState.initialAmount,
                onValueChange = { viewModel.updateInitialAmount(it) },
                label = { Text("Стартовый взнос (₽)") },
                modifier = Modifier.fillMaxWidth(),
                isError = inputState.error != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputState.periodMonths,
                onValueChange = { viewModel.updatePeriodMonths(it) },
                label = { Text("Срок вклада (месяцев)") },
                modifier = Modifier.fillMaxWidth(),
                isError = inputState.error != null
            )

            if (inputState.error != null) {
                Text(
                    text = inputState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = {
                        if (inputState.isValid) {
                            viewModel.navigateTo(Screen.Step2)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = inputState.isValid
                ) {
                    Text("Далее")
                }
            }
        }
    }
}

// Этап 2: Дополнительные параметры
@Composable
fun Step2Screen(viewModel: DepositViewModel) {
    val inputState by viewModel.inputState.collectAsState()
    val months = inputState.periodMonths.toIntOrNull() ?: 0
    val availableRate = viewModel.getInterestRate(months)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дополнительно") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Step1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Процентная ставка", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (months > 0) "$availableRate% годовых" else "Укажите срок вклада",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputState.monthlyTopUp,
                onValueChange = { viewModel.updateMonthlyTopUp(it) },
                label = { Text("Ежемесячное пополнение (₽, необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.Step1) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = { viewModel.calculateResult() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}

// Экран результата
@Composable
fun ResultScreen(viewModel: DepositViewModel) {
    val result by viewModel.resultState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "В начало")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Исправление 1: Добавлен OptIn для cardElevation
            @OptIn(ExperimentalMaterial3Api::class)
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ResultRow("Стартовый взнос:", "${result.initialAmount} ₽")
                    ResultRow("Срок вклада:", "${result.periodMonths} мес.")
                    ResultRow("Процентная ставка:", "${result.interestRate}%")

                    // Исправление 2: Сохраняем значение в локальную переменную для smart cast
                    val topUp = result.monthlyTopUp
                    if (topUp != null && topUp > 0) {
                        ResultRow("Ежемесячное пополнение:", "${topUp} ₽")
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ResultRow("Начисленные проценты:", "${String.format("%.2f", result.interestEarned)} ₽", bold = true)
                    ResultRow("Итоговая сумма:", "${String.format("%.2f", result.finalAmount)} ₽", bold = true, large = true)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Дата расчёта: ${result.calculationDate}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.saveCalculation() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }

                Button(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, bold: Boolean = false, large: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = if (large) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (large) 20.sp else 16.sp
        )
    }
}

// Экран истории
@Composable
fun HistoryScreen(viewModel: DepositViewModel) {
    val history by viewModel.historyList.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(history) { deposit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            // Пока просто заглушка, можно реализовать переход к деталям позже
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            Text(
                                text = dateFormat.format(Date(deposit.calculationDate)),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Взнос: ${deposit.initialAmount} ₽ → Итого: ${String.format("%.2f", deposit.finalAmount)} ₽",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// Экран деталей (заглушка)
@Composable
fun DetailScreen(viewModel: DepositViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Детальная информация о расчёте")
    }
}