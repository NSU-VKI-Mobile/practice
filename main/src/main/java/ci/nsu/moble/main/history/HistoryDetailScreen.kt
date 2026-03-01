package ci.nsu.moble.main.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.data.DepositCalculationEntity
import ci.nsu.moble.main.data.DepositRepository

@Composable
fun HistoryDetailScreen(
    repository: DepositRepository,
    calculationId: Long,
    onBack: () -> Unit
) {
    val itemState = produceState<DepositCalculationEntity?>(initialValue = null, calculationId) {
        value = repository.getCalculationById(calculationId)
    }

    val item = itemState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Детали расчёта",
            style = MaterialTheme.typography.headlineSmall
        )

        if (item == null) {
            Text("Загрузка...")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Дата и время: ${item.dateTime}")
                    Text("Стартовый взнос: ${item.initialAmount}")
                    Text("Срок вклада: ${item.months} мес.")
                    Text("Процентная ставка: ${item.ratePercent}%")
                    Text("Ежемесячное пополнение: ${item.monthlyTopUp}")
                    Text("Итоговая сумма: ${item.finalAmount}")
                    Text("Начисленные проценты: ${item.interestAmount}")
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}