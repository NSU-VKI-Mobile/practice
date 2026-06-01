package ci.nsu.mobile.calculations.ui

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
import ci.nsu.mobile.domain.model.DepositCalculation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: DepositViewModel, currentUsername: String?) {
    val calculations by viewModel.calculations.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (!currentUsername.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Аккаунт: $currentUsername", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        if (calculations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет сохраненных расчетов")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(calculations) { calc ->
                    CalculationItem(calc, onDelete = { viewModel.deleteCalculation(calc.id) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CalculationItem(calc: DepositCalculation, onDelete: () -> Unit) {
    val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Дата: ${df.format(Date(calc.calculationDate))}", style = MaterialTheme.typography.bodySmall)
                Text("Сумма: ${calc.initialAmount}", style = MaterialTheme.typography.titleMedium)
                Text("Итог: ${String.format("%.2f", calc.finalAmount)}", style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}