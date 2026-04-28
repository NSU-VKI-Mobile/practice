package ci.nsu.moble.main.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.moble.main.data.DepositCalculation
import ci.nsu.moble.main.presentation.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val calculations by viewModel.calculations.collectAsState()
    var selectedItem by remember { mutableStateOf<DepositCalculation?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(androidx.compose.material.icons.Icons.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Расчётов пока нет", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(calculations, key = { it.id }) { item ->
                    HistoryItem(
                        calculation = item,
                        onClick = { selectedItem = item }
                    )
                }
            }
        }
    }

    // Диалог с детальной информацией
    selectedItem?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = { Text("Детали расчёта") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Дата: ${formatDate(item.calculationDate)}")
                    Text("Взнос: ${item.initialAmount} ₽")
                    Text("Срок: ${item.periodMonths} мес.")
                    Text("Ставка: ${(item.interestRate * 100).toInt()}%")
                    Text("Пополнение: ${item.monthlyTopUp ?: 0.0} ₽/мес")
                    Text("Проценты: ${item.interestEarned} ₽")
                    Text("Итого: ${item.finalAmount} ₽", style = MaterialTheme.typography.bodyLarge)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
private fun HistoryItem(calculation: DepositCalculation, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = " ${formatDate(calculation.calculationDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Взнос: ${calculation.initialAmount} ₽  →  Итого: ${calculation.finalAmount} ₽",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}