package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    // Используем collectAsState для наблюдения за StateFlow
    val initialAmount by viewModel.initialAmount.collectAsState()
    val periodMonths by viewModel.periodMonths.collectAsState()
    val interestRate by viewModel.interestRate.collectAsState()
    val monthlyTopUp by viewModel.monthlyTopUp.collectAsState()
    val finalAmount by viewModel.finalAmount.collectAsState()
    val interestEarned by viewModel.interestEarned.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Результат", fontSize = 24.sp, modifier = Modifier.padding(vertical = 32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: $initialAmount руб.")
                Text("Срок: $periodMonths мес.")
                Text("Ставка: ${interestRate ?: 0}%")
                Text("Пополнение: ${if (monthlyTopUp.isBlank()) "0" else monthlyTopUp} руб/мес")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Итоговая сумма: ${String.format("%.2f", finalAmount)} руб.")
                Text("Начисленные проценты: ${String.format("%.2f", interestEarned)} руб.")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) { Text("Сохранить") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onHomeClick, modifier = Modifier.fillMaxWidth()) { Text("В начало") }
    }
}