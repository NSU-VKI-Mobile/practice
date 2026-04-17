package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.database.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDetailScreen(
    calculation: DepositCalculation?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("← Назад к списку")
        }

        // Содержимое
        if (calculation == null) {
            Text("Расчёт не найден", modifier = Modifier.padding(16.dp))
            return
        }

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(calculation.calculationDate))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Дата: $dateStr", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Стартовый взнос: ${calculation.initialAmount} руб.", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Срок вклада: ${calculation.periodMonths} месяцев", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Процентная ставка: ${calculation.interestRate}%", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))

                if (calculation.monthlyTopUp != null && calculation.monthlyTopUp > 0) {
                    Text(text = "Ежемесячное пополнение: ${calculation.monthlyTopUp} руб.", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                }

                Text(text = "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} руб.", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "Начисленные проценты: ${String.format("%.2f", calculation.interestEarned)} руб.", fontSize = 18.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}