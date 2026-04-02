package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.DecimalFormat

@Composable
fun HistoryScreen(
    viewModel: DepositViewModel,
    onItemClick: (Long) -> Unit,
    onBackToMain: () -> Unit
) {
    val calculations by viewModel.calculations.collectAsState()
    val decimalFormat = DecimalFormat("#,##0.00")

    // Загружаем историю при первом показе
    LaunchedEffect(Unit) {
        viewModel.loadCalculations()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        onClick = { onItemClick(calculation.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToMain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

@Composable
fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = calculation.getFormattedDate(),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "💰 ${decimalFormat.format(calculation.initialAmount)} руб → ${decimalFormat.format(calculation.finalAmount)} руб",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "📅 ${calculation.periodMonths} мес, ${calculation.interestRate}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}