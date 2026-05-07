package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.presentation.viewmodel.ResultUiState
import ci.nsu.mobile.main.presentation.viewmodel.ResultViewModel

@Composable
fun ResultScreen(
    amount: Double,
    months: Int,
    rate: Double,
    topUp: Double,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }

    val viewModel: ResultViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ResultViewModel(database) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.calculateAndSave(amount, months, rate, topUp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.headlineMedium)

        when (uiState) {
            is ResultUiState.Loading -> {
                CircularProgressIndicator()
                Text("Считаем проценты...")
            }

            is ResultUiState.Success -> {
                val calc = (uiState as ResultUiState.Success).calculation

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text(
                            text = "📥 Входные параметры",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Стартовый взнос: ${"%.0f".format(amount)} ₽")
                        Text("Срок вклада: $months мес.")
                        Text("Процентная ставка: ${"%.2f".format(rate)}%")
                        Text("Ежемес. пополнение: ${"%.0f".format(topUp)} ₽")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text(
                            text = "📈 Итоги расчёта",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Итоговая сумма: ${"%.2f".format(calc.finalAmount)} ₽",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Начисленные проценты: ${"%.2f".format(calc.interestEarned)} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()

                        Text(
                            text = "Дата расчёта: ${calc.calculationDate}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✅ Расчёт сохранён в историю")
                }

                Button(onClick = onNavigateHome, modifier = Modifier.fillMaxWidth()) {
                    Text("В начало")
                }
            }

            is ResultUiState.Error -> {
                Text("Ошибка: ${(uiState as ResultUiState.Error).message}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateHome) { Text("На главную") }
            }

            ResultUiState.Idle -> {
                CircularProgressIndicator()
            }
        }
    }
}