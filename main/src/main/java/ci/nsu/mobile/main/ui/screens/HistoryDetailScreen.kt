package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.DecimalFormat

@Composable
fun HistoryDetailScreen(
    viewModel: DepositViewModel,
    calculationId: Long,
    onBack: () -> Unit
) {
    val calculations by viewModel.calculations.collectAsState()
    val calculation = calculations.find { it.id == calculationId }
    val decimalFormat = DecimalFormat("#,##0.00")

    if (calculation == null) {
        // Если не нашли, возвращаемся
        onBack()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "📅 Дата: ${calculation.getFormattedDate()}",
                    style = MaterialTheme.typography.titleMedium
                )

                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("💰 Стартовый взнос:")
                    Text("${decimalFormat.format(calculation.initialAmount)} руб")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📅 Срок вклада:")
                    Text("${calculation.periodMonths} месяцев")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📈 Процентная ставка:")
                    Text("${calculation.interestRate}%")
                }

                if (calculation.monthlyTopUp != null && calculation.monthlyTopUp > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("➕ Ежемесячное пополнение:")
                        Text("${decimalFormat.format(calculation.monthlyTopUp)} руб")
                    }
                }

                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🏦 Итоговая сумма:")
                    Text(
                        "${decimalFormat.format(calculation.finalAmount)} руб",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📊 Начисленные проценты:")
                    Text(
                        "${decimalFormat.format(calculation.interestEarned)} руб",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}