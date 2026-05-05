package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDetailScreen(viewModel: DepositViewModel, onBack: () -> Unit) {
    val calc = viewModel.selectedCalculation
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("Назад") }
        Spacer(modifier = Modifier.height(16.dp))

        if (calc == null) {
            Text("Данные не найдены")
        } else {
            Text("Детали расчёта", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Дата расчёта: ${dateFormat.format(Date(calc.calculationDate))}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Стартовый взнос: ${calc.initialAmount}")
                    Text("Срок вклада: ${calc.periodMonths} мес.")
                    Text("Процентная ставка: ${calc.interestRate}%")
                    Text("Ежемесячное пополнение: ${calc.monthlyTopUp}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Начисленные проценты: ${String.format("%.2f", calc.interestEarned)}")
                    Text("Итоговая сумма: ${String.format("%.2f", calc.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}