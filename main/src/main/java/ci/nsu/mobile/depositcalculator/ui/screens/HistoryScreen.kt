package ci.nsu.mobile.depositcalculator.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.depositcalculator.data.database.DepositCalculationEntity
import ci.nsu.mobile.depositcalculator.data.repository.DepositRepository
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    repository: DepositRepository,
    onNavigateBack: () -> Unit
) {
    var calculations by remember { mutableStateOf<List<DepositCalculationEntity>>(emptyList()) }
    var selectedCalculation by remember { mutableStateOf<DepositCalculationEntity?>(null) }

    // Загрузка данных из базы
    LaunchedEffect(Unit) {
        repository.getAllCalculations().collectLatest { list ->
            calculations = list
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет сохранённых расчётов",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calculation ->
                    HistoryItemCard(
                        calculation = calculation,
                        onClick = { selectedCalculation = calculation }
                    )
                }
            }
        }
    }

    // Диалог с детальной информацией
    selectedCalculation?.let { calculation ->
        DetailDialog(
            calculation = calculation,
            onDismiss = { selectedCalculation = null }
        )
    }
}

@Composable
fun HistoryItemCard(
    calculation: DepositCalculationEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatTimestamp(calculation.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Стартовый: ${String.format("%.2f", calculation.amount)} ₽",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Итоговая: ${String.format("%.2f", calculation.finalAmount)} ₽",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DetailDialog(
    calculation: DepositCalculationEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Детали расчёта") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📅 Дата: ${formatTimestamp(calculation.timestamp)}")
                Text(text = "💰 Стартовый взнос: ${String.format("%.2f", calculation.amount)} ₽")
                Text(text = "📆 Срок: ${calculation.termMonths} месяцев")
                Text(text = "📈 Процентная ставка: ${String.format("%.2f", calculation.interestRate)}%")
                Text(text = "🔄 Капитализация: ${if (calculation.capitalization) "Да" else "Нет"}")

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "💰 Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "📊 Полученная прибыль: ${String.format("%.2f", calculation.profit)} ₽",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}
// Вспомогательная функция для форматирования timestamp в дату
private fun formatTimestamp(timestamp: Long): String {
    return try {
        val date = Date(timestamp)
        SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        "Дата не указана"
    }
}
