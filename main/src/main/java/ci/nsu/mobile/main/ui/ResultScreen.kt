package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Результат", fontSize = 24.sp, modifier = Modifier.padding(vertical = 32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${viewModel.initialAmountStr} руб.")
                Text("Срок: ${viewModel.periodMonthsStr} мес.")
                Text("Ставка: ${viewModel.interestRate}%")
                Text("Пополнение: ${viewModel.monthlyTopUpStr.ifBlank { "0" }} руб/мес")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Итоговая сумма: ${String.format("%.2f", viewModel.finalAmount)} руб.")
                Text("Начисленные проценты: ${String.format("%.2f", viewModel.interestEarned)} руб.")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) { Text("Сохранить") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onHomeClick, modifier = Modifier.fillMaxWidth()) { Text("В начало") }
    }
}