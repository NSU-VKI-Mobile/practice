package ci.nsu.moble.main.deposit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StepTwoScreen(
    viewModel: DepositViewModel,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val months = uiState.months.toIntOrNull()
    val rateText = when {
        months == null -> "Срок не указан"
        months < 6 -> "15%"
        months < 12 -> "10%"
        else -> "5%"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Этап 2: Дополнительные параметры",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Доступная процентная ставка: $rateText",
                modifier = Modifier.padding(16.dp)
            )
        }

        OutlinedTextField(
            value = uiState.monthlyTopUp,
            onValueChange = viewModel::updateMonthlyTopUp,
            label = { Text("Ежемесячное пополнение (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }

        Button(
            onClick = {
                if (viewModel.validateStepTwo()) {
                    viewModel.calculateDeposit()
                    onCalculate()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
    }
}