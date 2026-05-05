package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: DepositViewModel, onBack: () -> Unit, onNavigateToDetail: () -> Unit) {
    val historyList by viewModel.history.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) { Text("<") }
            Spacer(modifier = Modifier.width(16.dp))
            Text("История расчётов", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(historyList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // Сохраняем выбранный элемент во ViewModel и переходим на детали
                            viewModel.selectedCalculation = item
                            onNavigateToDetail()
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(dateFormat.format(Date(item.calculationDate)), style = MaterialTheme.typography.labelSmall)
                        Text("Взнос: ${item.initialAmount} | Итог: ${String.format("%.2f", item.finalAmount)}")
                    }
                }
            }
        }
    }
}