package ci.nsu.mobile.main.presentation.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.DepositApplication
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    initialAmount: String,
    periodMonths: String,
    interestRate: Double?,
    monthlyTopUp: String?
) {
    val application = LocalContext.current.applicationContext as DepositApplication
    val viewModel: ResultViewModel = viewModel(
        factory = ResultViewModelFactory(
            application.locator.depositRepository,
            application.locator.calculateDepositUseCase,
            application.locator.tokenManager
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        interestRate?.let { rate ->
            viewModel.calculateDeposit(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = rate,
                monthlyTopUp = monthlyTopUp?.takeIf { it != "null" }
            )
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Результат расчёта") }) },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.saveCalculation() },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isSaved && uiState.calculation != null
                ) {
                    Text(if (uiState.isSaved) "Сохранено" else "Сохранить")
                }
            }
        }
    ) { paddingValues ->
        val formatter = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }

        uiState.calculation?.let { calculation ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResultRow("Стартовый взнос:", formatter.format(calculation.initialAmount))
                    ResultRow("Срок вклада:", "${calculation.periodMonths} мес.")
                    ResultRow("Процентная ставка:", "${calculation.interestRate}%")

                    calculation.monthlyTopUp?.let { topUp ->
                        ResultRow("Ежемесячное пополнение:", formatter.format(topUp))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ResultRow("Начисленные проценты:", formatter.format(calculation.interestEarned), isHighlighted = true)
                    ResultRow(
                        "Итоговая сумма:",
                        formatter.format(calculation.finalAmount),
                        isHighlighted = true,
                        valueStyle = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = valueStyle,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}