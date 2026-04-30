package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    amount: Double,
    months: Int,
    rate: Double,
    topUp: Double,
    onSave: () -> Unit,
    onGoHome: () -> Unit
) {
    val interestProfit = amount * (rate / 100) * (months.toDouble() / 12)
    val totalTopUp = topUp * months
    val finalAmount = amount + interestProfit + totalTopUp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultRow("Стартовый взнос:", "${"%.2f".format(amount)} ₽")
                ResultRow("Срок вклада:", "$months мес.")
                ResultRow("Процентная ставка:", "$rate%")
                if (topUp > 0) {
                    ResultRow("Ежемесячное пополнение:", "${"%.2f".format(topUp)} ₽")
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                ResultRow("Итоговая сумма:", "${"%.2f".format(finalAmount)} ₽", isHighlight = true)
                ResultRow("Начисленные проценты:", "${"%.2f".format(interestProfit)} ₽", isHighlight = true)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onGoHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}