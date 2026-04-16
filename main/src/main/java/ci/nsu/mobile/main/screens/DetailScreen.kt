package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.models.DepositCalculation
import ci.nsu.mobile.main.viewmodels.HistoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    calculationId: Long,
    viewModel: HistoryViewModel = viewModel(),
    onBack: () -> Unit
) {
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(calculationId) {
        calculation = viewModel.getCalculationById(calculationId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                    }
                }
            )
        }
    ) { paddingValues ->
        calculation?.let { calc ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailRow("Дата расчёта:", calc.formattedDate)
                        Divider()
                        DetailRow("Стартовый взнос:", String.format("%.2f ₽", calc.initialAmount))
                        DetailRow("Срок вклада:", "${calc.periodMonths} мес.")
                        DetailRow("Процентная ставка:", "${calc.interestRate}%")
                        calc.monthlyTopUp?.let {
                            DetailRow("Ежемесячное пополнение:", String.format("%.2f ₽", it))
                        }
                        Divider()
                        DetailRow(
                            "Итоговая сумма:",
                            String.format("%.2f ₽", calc.finalAmount),
                            isTotal = true
                        )
                        DetailRow(
                            "Начисленные проценты:",
                            String.format("%.2f ₽", calc.interestEarned),
                            isTotal = true
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isTotal: Boolean = false) {
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