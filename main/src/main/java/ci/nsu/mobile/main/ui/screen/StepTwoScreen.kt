package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.viewmodel.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(
    initialAmount: Double,
    periodMonths: Int,
    onNavigateToResult: (DepositCalculation) -> Unit,
    onNavigateBack: () -> Unit
) {
    val factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return CalculationViewModel(initialAmount, periodMonths) as T
        }
    }
    val viewModel: CalculationViewModel = viewModel(factory = factory)

    val availableRates by viewModel.availableRates.collectAsState()
    val selectedRate by viewModel.selectedRate.collectAsState()
    val monthlyTopUp by viewModel.monthlyTopUp.collectAsState()
    val rateErrorState by viewModel.rateError.collectAsState()
    val periodErrorState by viewModel.periodError.collectAsState()


    val periodError = periodErrorState
    val rateError = rateErrorState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дополнительные параметры") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (periodError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = periodError,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (availableRates.isNotEmpty()) {
                Text("Выберите процентную ставку:")

                availableRates.forEach { rate ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRate == rate,
                            onClick = { viewModel.updateSelectedRate(rate) }
                        )
                        Text(
                            text = "$rate%",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            } else if (periodError == null) {
                Text(
                    text = "Нет доступных ставок",
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (rateError != null) {
                Text(
                    text = rateError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            OutlinedTextField(
                value = monthlyTopUp,
                onValueChange = { viewModel.updateMonthlyTopUp(it) },
                label = { Text("Ежемесячное пополнение (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        viewModel.calculateDeposit()?.let { result ->
                            onNavigateToResult(result)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = availableRates.isNotEmpty() && periodError == null
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}