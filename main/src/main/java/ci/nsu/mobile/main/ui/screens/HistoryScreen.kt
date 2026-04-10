package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.presentation.viewmodel.HistoryUiState
import ci.nsu.mobile.main.presentation.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onNavigateBack: () -> Unit) {
    val viewModel: HistoryViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Заголовок с кнопкой назад
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onNavigateBack) { Text("Назад") }
            Spacer(modifier = Modifier.width(16.dp))
            Text("История расчётов", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is HistoryUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Загрузка истории...")
                    }
                }
            }

            is HistoryUiState.Success -> {
                val items = (uiState as HistoryUiState.Success).items

                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📭", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("История пуста", style = MaterialTheme.typography.titleMedium)
                            Text("Сделайте первый расчёт!", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    // Показываем количество записей
                    Text(
                        text = "Всего расчётов: ${items.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Список расчётов
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items, key = { it.id }) { item ->
                            HistoryItemCard(
                                item = item,
                                dateFormat = dateFormat
                            )
                        }
                    }
                }
            }

            is HistoryUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❌ Ошибка загрузки", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text((uiState as HistoryUiState.Error).message)
                    }
                }
            }
        }
    }
}

// Отдельный компонент для элемента списка
@Composable
fun HistoryItemCard(item: ci.nsu.mobile.main.data.database.DepCalcs, dateFormat: SimpleDateFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок: дата
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(Date(item.calculationDate)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Входные данные (компактно)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Взнос: ${item.initialAmount} ₽", style = MaterialTheme.typography.bodyMedium)
                    Text("Срок: ${item.periodMonths} мес.", style = MaterialTheme.typography.bodySmall)
                }

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("Ставка: ${item.interestRate}%", style = MaterialTheme.typography.bodyMedium)
                    item.monthlyTopUp?.let {
                        Text("Пополнение: $it ₽", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Итоги (выделено)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("Итого:", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "${"%.2f".format(item.finalAmount)} ₽",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Прибыль:", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "+${"%.2f".format(item.interestEarned)} ₽",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}