package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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

    val rateOptions = listOf(5, 10, 15)

    // Функция для получения минимального срока для выбранной ставки
    fun getMinPeriodForRate(rate: Int): Int {
        return when (rate) {
            5 -> 3
            10 -> 6
            15 -> 12
            else -> 3
        }
    }

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

            Text("Выберите процентную ставку:")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rateOptions.forEach { rate ->
                    Button(
                        onClick = {
                            // 1. Обновляем выбранную ставку
                            viewModel.updateSelectedRate(rate.toDouble())
                            // 2. updatePeriod(minPeriod) — НЕ НУЖНО!
                            //    Срок автоматически обновится внутри updateSelectedRate
                        },
                        modifier = Modifier.weight(1f),
                        colors = if (selectedRate == rate.toDouble()) {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        }
                    ) {
                        Text("$rate%")
                    }
                }
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