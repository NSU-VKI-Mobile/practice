package ci.nsu.mobile.depositcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.depositcalculator.data.database.DepositCalculationEntity
import ci.nsu.mobile.depositcalculator.data.repository.DepositRepository
import kotlinx.coroutines.launch
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double?,
    repository: DepositRepository,
    onSaveAndNavigateToMain: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showSaveSuccess by remember { mutableStateOf(false) }

    // Расчет итоговой суммы и процентов
    val monthlyRate = interestRate / 100 / 12
    var finalAmount = initialAmount
    var totalInterest = 0.0

    for (month in 1..periodMonths) {
        val monthlyInterest = finalAmount * monthlyRate
        totalInterest += monthlyInterest
        finalAmount += monthlyInterest
        if (monthlyTopUp != null && monthlyTopUp > 0) {
            finalAmount += monthlyTopUp
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчёта") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ResultCard(
                    title = "Стартовый взнос",
                    value = "${String.format("%.2f", initialAmount)} ₽"
                )
            }

            item {
                ResultCard(
                    title = "Срок вклада",
                    value = "$periodMonths месяцев"
                )
            }

            item {
                ResultCard(
                    title = "Процентная ставка",
                    value = "$interestRate%"
                )
            }

            if (monthlyTopUp != null && monthlyTopUp > 0) {
                item {
                    ResultCard(
                        title = "Ежемесячное пополнение",
                        value = "${String.format("%.2f", monthlyTopUp)} ₽"
                    )
                }
            }

            item {
                ResultCard(
                    title = "Итоговая сумма",
                    value = "${String.format("%.2f", finalAmount)} ₽",
                    isHighlighted = true
                )
            }

            item {
                ResultCard(
                    title = "Начисленные проценты",
                    value = "${String.format("%.2f", totalInterest)} ₽"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                val calculation = DepositCalculationEntity(
                                    amount = initialAmount,
                                    termMonths = periodMonths,
                                    interestRate = interestRate,
                                    capitalization = false,
                                    finalAmount = finalAmount,
                                    profit = totalInterest,
                                    timestamp = System.currentTimeMillis()
                                )
                                repository.saveCalculation(calculation)
                                showSaveSuccess = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Сохранить")
                    }

                    OutlinedButton(
                        onClick = onNavigateToMain,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("В начало")
                    }
                }
            }
        }
    }

    if (showSaveSuccess) {
        AlertDialog(
            onDismissRequest = {
                showSaveSuccess = false
                onSaveAndNavigateToMain()
            },
            title = { Text("Успешно") },
            text = { Text("Расчёт сохранён в истории") },
            confirmButton = {
                TextButton(onClick = {
                    showSaveSuccess = false
                    onSaveAndNavigateToMain()
                }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ResultCard(
    title: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = if (isHighlighted) 28.sp else 24.sp,
                color = if (isHighlighted)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
