package ci.nsu.mobile.calculations.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.calculations.ui.DepositViewModel
import ci.nsu.mobile.domain.model.DepositCalculation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DepositsTab(vm: DepositViewModel) {
    val history by vm.history.collectAsState()
    val fmt = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    if (history.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Нет сохраненных расчетов")
        }
        return
    }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(history, key = { it.id }) { item ->
            DepositCard(item, fmt, onDelete = { vm.deleteDeposit(item) })
        }
    }
}

@Composable
private fun DepositCard(
    item: DepositCalculation, fmt: SimpleDateFormat, onDelete: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "${item.startAmount} ₽ → ${String.format("%.2f", item.finalAmount)} ₽",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Срок: ${item.months} мес. | Ставка: ${item.rate}%", style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Прибыль: ${String.format("%.2f", item.profit)} ₽", style = MaterialTheme.typography.bodySmall
                )
                Text(
                    fmt.format(Date(item.date)), style = MaterialTheme.typography.labelSmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}