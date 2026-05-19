package ci.nsu.mobile.main.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.DepositCalculation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    calculation: DepositCalculation?,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Детали расчёта") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (calculation == null) {
                Text(
                    text = "Запись не найдена.",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InfoRow("Дата расчёта", formatDateTime(calculation.calculationDate))
                        InfoRow("Стартовый взнос", formatMoney(calculation.initialAmount))
                        InfoRow("Срок вклада", "${calculation.periodMonths} мес.")
                        InfoRow("Процентная ставка", formatPercent(calculation.interestRate))
                        InfoRow("Ежемесячное пополнение", formatTopUp(calculation.monthlyTopUp))
                        InfoRow("Итоговая сумма", formatMoney(calculation.finalAmount))
                        InfoRow("Начисленные проценты", formatMoney(calculation.interestEarned))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}