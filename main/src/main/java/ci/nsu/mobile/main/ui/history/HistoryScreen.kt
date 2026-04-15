package ci.nsu.mobile.main.ui.history

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import ci.nsu.mobile.ui.history.HistoryViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    context: Context,
    viewModel: HistoryViewModel = HistoryViewModel(context),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчетов") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("← Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.calculations.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет сохраненных расчетов")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.calculations,
                        key = { calculation -> calculation.id }
                    ) { calculation ->
                        CalculationCard(
                            calculation = calculation,
                            isExpanded = calculation == uiState.selectedCalculation,
                            onClick = {
                                if (calculation == uiState.selectedCalculation) {
                                    viewModel.clearSelectedCalculation()
                                } else {
                                    viewModel.selectCalculation(calculation)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculationCard(
    calculation: DepositCalculationEntity,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Верхняя часть (всегда видна)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = dateFormat.format(Date(calculation.calculationDate)),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${calculation.interestRate}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = currencyFormat.format(calculation.initialAmount),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = currencyFormat.format(calculation.finalAmount),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Разворачивающаяся часть (детали)
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Детали расчета",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text("Срок: ${calculation.periodMonths} месяцев")
                    if (calculation.monthlyTopUp > 0) {
                        Text("Пополнение: ${currencyFormat.format(calculation.monthlyTopUp)}/мес")
                    }
                    Text("Заработано: ${currencyFormat.format(calculation.interestEarned)}")
                }
            }
        }
    }
}