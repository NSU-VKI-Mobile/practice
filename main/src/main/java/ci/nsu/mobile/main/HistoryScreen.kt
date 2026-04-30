package ci.nsu.mobile.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.local.DepositCalculation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: DepositViewModel,
    onBack: () -> Unit
) {
    val history by viewModel.allCalculations.collectAsState()

    var selectedItem by remember { mutableStateOf<DepositCalculation?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
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
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("История пуста")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(history) { item ->
                    ListItem(
                        headlineContent = { Text("Итог: ${"%.2f".format(item.finalAmount)} ₽") },
                        supportingContent = {
                            Text("Дата: ${item.date} | Взнос: ${item.initialAmount} ₽")
                        },
                        modifier = Modifier
                            .clickable { selectedItem = item }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    selectedItem?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = { Text("Детальная информация") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Дата расчёта: ${item.date}")
                    Text("Стартовый взнос: ${item.initialAmount} ₽")
                    Text("Срок: ${item.months} мес.")
                    Text("Ставка: ${item.rate}%")
                    Text("Ежемесячное пополнение: ${item.monthlyTopUp} ₽")
                    HorizontalDivider()
                    Text("Начислено процентов: ${"%.2f".format(item.profit)} ₽")
                    Text("Итоговая сумма: ${"%.2f".format(item.finalAmount)} ₽", style = MaterialTheme.typography.titleMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("ОК")
                }
            }
        )
    }
}