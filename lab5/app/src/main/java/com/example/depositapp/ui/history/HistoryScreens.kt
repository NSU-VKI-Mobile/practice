package com.example.depositapp.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.depositapp.data.db.DepositCalculation
import com.example.depositapp.ui.DepositViewModel
import kotlinx.coroutines.flow.first
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Форматирование даты: 1714000000000L → "25.04.2024 14:30"
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    return sdf.format(Date(timestamp))
}

private fun formatMoney(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    fmt.minimumFractionDigits = 2
    fmt.maximumFractionDigits = 2
    return "${fmt.format(amount)} ₽"
}

// Экран истории расчётов
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: DepositViewModel,
    onBack:   () -> Unit,
    onDetail: (Long) -> Unit  // передаём id выбранной записи
) {
    // collectAsStateWithLifecycle для Flow из базы данных
    // Список автоматически обновится при добавлении нового расчёта
    val history by viewModel.history.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (history.isEmpty()) {
            // Пустое состояние — нет ни одного расчёта
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("История пуста", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Рассчитайте вклад и сохраните результат",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // LazyColumn — аналог RecyclerView, рендерит только видимые элементы
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // items — функция для рендера списка
                items(history) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        onClick = { onDetail(calculation.id) }
                    )
                }
            }
        }
    }
}

// Карточка одного элемента истории
@Composable
private fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // .clickable — делаем карточку кликабельной
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDate(calculation.calculationDate),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${calculation.interestRate}% на ${calculation.periodMonths} мес.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Взнос: ${formatMoney(calculation.initialAmount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Итого: ${formatMoney(calculation.finalAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Экран детального просмотра одного расчёта
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Long,
    viewModel: DepositViewModel,
    onBack: () -> Unit
) {
    // remember — значение сохраняется при перерисовке
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }

    // LaunchedEffect — запускает код при первом появлении composable
    // key = id — перезапускается если id изменится
    LaunchedEffect(id) {
        // Загружаем расчёт из базы данных
        calculation = viewModel.history.first().find { it.id == id }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        calculation?.let { calc ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatDate(calc.calculationDate),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DetailRow("Стартовый взнос", formatMoney(calc.initialAmount))
                        DetailRow("Срок вклада", "${calc.periodMonths} месяцев")
                        DetailRow("Процентная ставка", "${calc.interestRate}% годовых")
                        calc.monthlyTopUp?.let { topUp ->
                            if (topUp > 0) DetailRow("Ежем. пополнение", formatMoney(topUp))
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        DetailRow("Итоговая сумма", formatMoney(calc.finalAmount), highlight = true)
                        DetailRow("Начислено процентов", formatMoney(calc.interestEarned))
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = value,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}
