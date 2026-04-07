package ci.nsu.moble.main.deposit

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    onBackHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${uiState.initialAmount}")
                Text("Срок вклада: ${uiState.months} мес.")
                Text("Процентная ставка: ${uiState.ratePercent ?: "-"}%")
                Text(
                    "Ежемесячное пополнение: ${
                        if (uiState.monthlyTopUp.isBlank()) "0" else uiState.monthlyTopUp
                    }"
                )
                Text("Итоговая сумма: ${uiState.finalAmount ?: 0.0}")
                Text("Начисленные проценты: ${uiState.interestAmount ?: 0.0}")
            }
        }

        if (uiState.saveMessage != null) {
            Text(
                text = uiState.saveMessage!!,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = { viewModel.saveCalculation() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Button(
            onClick = {
                viewModel.resetAll()
                onBackHome()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}