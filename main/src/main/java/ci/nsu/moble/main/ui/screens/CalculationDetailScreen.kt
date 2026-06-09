package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.data.models.DepositCalculation
import java.text.DecimalFormat

@Composable
fun CalculationDetailScreen(
    calculation: DepositCalculation,
    onBack: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Детали расчёта",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("📅 Дата: ${calculation.getFormattedDate()}")
                Text("💰 Стартовый взнос: ${decimalFormat.format(calculation.initialAmount)} руб")
                Text("📅 Срок: ${calculation.periodMonths} месяцев")
                Text("📈 Ставка: ${calculation.interestRate}%")
                if (calculation.monthlyTopUp != null && calculation.monthlyTopUp > 0) {
                    Text("➕ Пополнение: ${decimalFormat.format(calculation.monthlyTopUp)} руб/мес")
                }
                Divider()
                Text(
                    text = "🏦 Итоговая сумма: ${decimalFormat.format(calculation.finalAmount)} руб",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "📊 Начислено: ${decimalFormat.format(calculation.interestEarned)} руб",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}