package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.db.DepositCalculation
import ci.nsu.mobile.main.di.LocalRepository
import ci.nsu.mobile.main.ui.viewmodel.HistoryViewModel
import ci.nsu.mobile.main.util.formatCurrency
import ci.nsu.mobile.main.util.formatDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    calculationId: Long,
    onBack: () -> Unit
) {
    val repository = LocalRepository.current
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(repository)
    )
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }

    LaunchedEffect(calculationId) {
        calculation = viewModel.getCalculationById(calculationId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        calculation?.let { calc ->
            Card(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Дата: ${calc.calculationDate.formatDate()}")
                    Text("Стартовый взнос: ${calc.initialAmount.formatCurrency()}")
                    Text("Срок: ${calc.periodMonths} мес.")
                    Text("Ставка: ${calc.interestRate}%")
                    if (calc.monthlyTopUp != null) {
                        Text("Ежемес. пополнение: ${calc.monthlyTopUp.formatCurrency()}")
                    } else {
                        Text("Без пополнений")
                    }
                    Text("Итог: ${calc.finalAmount.formatCurrency()}")
                    Text("Проценты: ${calc.interestEarned.formatCurrency()}")
                }
            }
        } ?: Box(modifier = Modifier.padding(padding)) {
            Text("Загрузка...")
        }
    }
}