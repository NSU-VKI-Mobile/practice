package ci.nsu.mobile.main.calculations.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.calculations.data.database.DepositCalculationEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCalculation by viewModel.selectedCalculation.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var calculationToDelete by remember { mutableStateOf<DepositCalculationEntity?>(null) }
    var showFilters by remember { mutableStateOf(false) }

    var amountMinText by remember { mutableStateOf("") }
    var amountMaxText by remember { mutableStateOf("") }
    var termMinText by remember { mutableStateOf("") }
    var termMaxText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои расчёты") },
                actions = {
                    TextButton(onClick = { showFilters = !showFilters }) {
                        Text(if (showFilters) "Скрыть фильтры" else "Фильтры")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showFilters) {
                FilterPanel(
                    amountMin = amountMinText,
                    amountMax = amountMaxText,
                    termMin = termMinText,
                    termMax = termMaxText,
                    onAmountMinChange = { amountMinText = it },
                    onAmountMaxChange = { amountMaxText = it },
                    onTermMinChange = { termMinText = it },
                    onTermMaxChange = { termMaxText = it },
                    onApply = {
                        viewModel.setAmountRange(amountMinText, amountMaxText)
                        viewModel.setTermRange(termMinText, termMaxText)
                        showFilters = false
                    },
                    onClear = {
                        amountMinText = ""
                        amountMaxText = ""
                        termMinText = ""
                        termMaxText = ""
                        viewModel.clearFilters()
                    }
                )
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.calculations.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Нет сохраненных расчетов")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.calculations,
                            key = { it.id }
                        ) { calculation ->
                            CalculationCard(
                                calculation = calculation,
                                isExpanded = calculation == selectedCalculation,
                                onClick = {
                                    if (calculation == selectedCalculation) {
                                        viewModel.clearSelectedCalculation()
                                    } else {
                                        viewModel.selectCalculation(calculation)
                                    }
                                },
                                onDelete = {
                                    calculationToDelete = calculation
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && calculationToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить расчет?") },
            text = { Text("Это действие нельзя отменить.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCalculation(calculationToDelete!!)
                        showDeleteDialog = false
                        calculationToDelete = null
                    }
                ) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun FilterPanel(
    amountMin: String,
    amountMax: String,
    termMin: String,
    termMax: String,
    onAmountMinChange: (String) -> Unit,
    onAmountMaxChange: (String) -> Unit,
    onTermMinChange: (String) -> Unit,
    onTermMaxChange: (String) -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Фильтры", style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = amountMin,
                    onValueChange = onAmountMinChange,
                    label = { Text("Сумма от") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = amountMax,
                    onValueChange = onAmountMaxChange,
                    label = { Text("Сумма до") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = termMin,
                    onValueChange = onTermMinChange,
                    label = { Text("Срок от (мес)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = termMax,
                    onValueChange = onTermMaxChange,
                    label = { Text("Срок до (мес)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onApply, modifier = Modifier.weight(1f)) {
                    Text("Применить")
                }
                OutlinedButton(onClick = onClear, modifier = Modifier.weight(1f)) {
                    Text("Сбросить")
                }
            }
        }
    }
}

@Composable
fun CalculationCard(
    calculation: DepositCalculationEntity,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp)
        ) {
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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Spacer(modifier = Modifier.width(8.dp))

                    // Кнопка удаления
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Удалить")
                    }
                }
            }

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