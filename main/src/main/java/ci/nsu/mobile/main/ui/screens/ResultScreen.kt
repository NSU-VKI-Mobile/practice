package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.ui.LocalDepositViewModel
import ci.nsu.mobile.main.util.formatCurrency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onHome: () -> Unit
) {
    val viewModel = LocalDepositViewModel.current
    val result by viewModel.result.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Результат расчёта") })
        }
    ) { padding ->
        result?.let { calc ->
            Card(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Стартовый взнос: ${calc.initialAmount.formatCurrency()}")
                    Text("Срок: ${calc.periodMonths} мес.")
                    Text("Ставка: ${calc.interestRate}%")
                    if (calc.monthlyTopUp != null) {
                        Text("Ежемес. пополнение: ${calc.monthlyTopUp.formatCurrency()}")
                    } else {
                        Text("Без пополнений")
                    }
                    Text("Итоговая сумма: ${calc.finalAmount.formatCurrency()}")
                    Text("Начислено процентов: ${calc.interestEarned.formatCurrency()}")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.saveCalculation()
                            snackbarHostState.showSnackbar("Расчёт сохранён")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }
            }
        } ?: Box(modifier = Modifier.padding(padding)) {
            Text("Нет данных для отображения")
        }
    }
}