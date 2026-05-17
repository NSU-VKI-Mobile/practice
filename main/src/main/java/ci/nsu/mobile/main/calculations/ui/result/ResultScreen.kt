package ci.nsu.mobile.main.calculations.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onNavigateToMain: () -> Unit
) {
    val viewModel: ResultViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
    val percentFormat = NumberFormat.getPercentInstance(Locale("ru", "RU")).apply {
        minimumFractionDigits = 1
        maximumFractionDigits = 1
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчета") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Основная карточка с результатом
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Итоговая сумма",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currencyFormat.format(uiState.finalAmount),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            // Карточка с деталями
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Детали расчета",
                        style = MaterialTheme.typography.titleMedium
                    )

                    DetailRow(
                        label = "Стартовый взнос",
                        value = currencyFormat.format(uiState.initialAmount)
                    )

                    DetailRow(
                        label = "Срок вклада",
                        value = "${uiState.term} ${getMonthWord(uiState.term)}"
                    )

                    DetailRow(
                        label = "Процентная ставка",
                        value = percentFormat.format(uiState.rate / 100)
                    )

                    if (uiState.monthlyAddition > 0) {
                        DetailRow(
                            label = "Ежемесячное пополнение",
                            value = currencyFormat.format(uiState.monthlyAddition)
                        )
                    }

                    Divider()

                    DetailRow(
                        label = "Начисленные проценты",
                        value = currencyFormat.format(uiState.earnedInterest),
                        isHighlighted = true
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.saveCalculationToDatabase() },
                    enabled = !uiState.isSaved
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

@Composable
fun DetailRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = if (isHighlighted) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            color = if (isHighlighted) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

fun getMonthWord(months: Int): String {
    return when {
        months % 10 == 1 && months % 100 != 11 -> "месяц"
        months % 10 in 2..4 && (months % 100 !in 12..14) -> "месяца"
        else -> "месяцев"
    }
}