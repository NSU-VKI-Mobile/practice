package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.viewmodel.ResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: DepositCalculation,
    onNavigateToMain: () -> Unit
) {
    val factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ResultViewModel(result) as T
        }
    }
    val viewModel: ResultViewModel = viewModel(factory = factory)
    val isSaved by viewModel.isSaved.collectAsState()

    LaunchedEffect(isSaved) {
        if (isSaved) onNavigateToMain()
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Стартовый взнос: ${String.format("%.2f", result.initialAmount)} ₽",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Срок вклада: ${result.periodMonths} месяцев",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Процентная ставка: ${result.interestRate}%",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (result.monthlyTopUp != null && result.monthlyTopUp > 0) {
                        Text(
                            text = "Ежемесячное пополнение: ${String.format("%.2f", result.monthlyTopUp)} ₽",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Начисленные проценты: ${String.format("%.2f", result.interestEarned)} ₽",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Итоговая сумма: ${String.format("%.2f", result.finalAmount)} ₽",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }

                Button(
                    onClick = onNavigateToMain,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }
            }
        }
    }
}