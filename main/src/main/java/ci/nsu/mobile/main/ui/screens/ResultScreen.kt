package ci.nsu.mobile.main.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val viewModel = LocalDepositViewModel.current
    val result by viewModel.result.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Результат расчёта") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (result != null) {
                    val calc = result!!
                    Card(
                        modifier = Modifier.fillMaxWidth()
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
                } else {
                    Text(
                        "Нет данных для отображения",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (result != null) {
                            scope.launch {
                                viewModel.saveCalculation()
                                snackbarHostState.showSnackbar("Расчёт сохранён")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Нет данных для сохранения")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = result != null
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
        }
    }
}