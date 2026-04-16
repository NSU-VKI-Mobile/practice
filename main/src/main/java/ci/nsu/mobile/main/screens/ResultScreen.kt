package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodels.ResultViewModel
import ci.nsu.mobile.main.viewmodels.ResultViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double?,
    repository: DepositRepository,
    viewModel: ResultViewModel = viewModel(
        factory = ResultViewModelFactory(repository)
    ),
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var saved by remember { mutableStateOf(false) }

    val (finalAmount, interestEarned) = remember(initialAmount, periodMonths, interestRate, monthlyTopUp) {
        repository.calculateDeposit(initialAmount, periodMonths, interestRate, monthlyTopUp)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчёта") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultRow("Стартовый взнос:", String.format("%.2f ₽", initialAmount))
                    ResultRow("Срок вклада:", "$periodMonths мес.")
                    ResultRow("Процентная ставка:", "$interestRate%")
                    monthlyTopUp?.let {
                        ResultRow("Ежемесячное пополнение:", String.format("%.2f ₽", it))
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ResultRow(
                        "Итоговая сумма:",
                        String.format("%.2f ₽", finalAmount),
                        isTotal = true
                    )
                    ResultRow(
                        "Начисленные проценты:",
                        String.format("%.2f ₽", interestEarned),
                        isTotal = true
                    )
                }
            }

            if (saved) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Расчёт успешно сохранён!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = {
                        viewModel.saveCalculation(
                            initialAmount = initialAmount,
                            periodMonths = periodMonths,
                            interestRate = interestRate,
                            monthlyTopUp = monthlyTopUp,
                            finalAmount = finalAmount,
                            interestEarned = interestEarned
                        ) {
                            saved = true
                            onSave()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !saved
                ) {
                    Text(if (saved) "Сохранено" else "Сохранить")
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}