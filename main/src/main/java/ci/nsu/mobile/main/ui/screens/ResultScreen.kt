package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel

@Composable
fun ResultScreen(viewModel: DepositViewModel, onSave: () -> Unit, onGoHome: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Результат", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${viewModel.initialAmount}")
                Text("Срок вклада: ${viewModel.periodMonths} мес.")
                Text("Ставка: ${viewModel.currentRate}%")
                Text("Ежемес. пополнение: ${viewModel.monthlyTopUp.ifBlank { "0" }}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Начисленные проценты: ${String.format("%.2f", viewModel.interestEarned)}")
                Text("Итоговая сумма: ${String.format("%.2f", viewModel.finalAmount)}", style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = {
                viewModel.resetData()
                onGoHome()
            }) { Text("В начало") }
            Button(onClick = {
                viewModel.saveCalculation()
                viewModel.resetData()
                onSave()
            }) { Text("Сохранить") }
        }
    }
}