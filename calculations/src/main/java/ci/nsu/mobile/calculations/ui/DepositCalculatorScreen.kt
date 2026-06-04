package ci.nsu.mobile.calculations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.calculations.data.local.entities.DepositCalculation
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.calculations.viewmodel.DepositResult
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositCalculatorScreen(
    depositViewModel: DepositViewModel,
    userId: Long,
    onSaveSuccess: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var monthlyTopUp by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<DepositResult?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val isLoading by depositViewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        depositViewModel.setUserId(userId)
    }

    LaunchedEffect(Unit) {
        depositViewModel.saveSuccess.collectLatest {
            isSaving = false
            onSaveSuccess()
        }
        depositViewModel.error.collectLatest { error ->
            isSaving = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Deposit Calculator",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        when (step) {
            1 -> Step1Input(
                initialAmount = initialAmount,
                onInitialAmountChange = { initialAmount = it },
                periodMonths = periodMonths,
                onPeriodMonthsChange = { periodMonths = it },
                onNext = {
                    if (initialAmount.isNotBlank() && periodMonths.isNotBlank()) {
                        step = 2
                    }
                }
            )
            2 -> Step2Input(
                interestRate = interestRate,
                onInterestRateChange = { interestRate = it },
                monthlyTopUp = monthlyTopUp,
                onMonthlyTopUpChange = { monthlyTopUp = it },
                onBack = { step = 1 },
                onCalculate = {
                    val initial = initialAmount.toDoubleOrNull() ?: 0.0
                    val months = periodMonths.toIntOrNull() ?: 0
                    val rate = interestRate.toDoubleOrNull() ?: 0.0
                    val topUp = monthlyTopUp.toDoubleOrNull()

                    val calculation = depositViewModel.calculateDeposit(
                        initialAmount = initial,
                        periodMonths = months,
                        interestRate = rate,
                        monthlyTopUp = topUp
                    )
                    result = calculation
                    step = 3
                }
            )
            3 -> Step3Result(
                initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
                periodMonths = periodMonths.toIntOrNull() ?: 0,
                interestRate = interestRate.toDoubleOrNull() ?: 0.0,
                monthlyTopUp = monthlyTopUp.toDoubleOrNull(),
                result = result,
                onBack = { step = 2 },
                onSave = {
                    isSaving = true
                    val calculation = DepositCalculation(
                        initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
                        periodMonths = periodMonths.toIntOrNull() ?: 0,
                        interestRate = interestRate.toDoubleOrNull() ?: 0.0,
                        monthlyTopUp = monthlyTopUp.toDoubleOrNull(),
                        finalAmount = result?.finalAmount ?: 0.0,
                        interestEarned = result?.interestEarned ?: 0.0,
                        calculationDate = System.currentTimeMillis(),
                        userId = userId
                    )
                    depositViewModel.saveCalculation(calculation)
                },
                isSaving = isSaving || isLoading
            )
        }
    }
}



@Composable
fun Step1Input(
    initialAmount: String,
    onInitialAmountChange: (String) -> Unit,
    periodMonths: String,
    onPeriodMonthsChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = initialAmount,
            onValueChange = onInitialAmountChange,
            label = { Text("Initial Amount (₽)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = periodMonths,
            onValueChange = onPeriodMonthsChange,
            label = { Text("Period (months)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = initialAmount.isNotBlank() && periodMonths.isNotBlank()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun Step2Input(
    interestRate: String,
    onInterestRateChange: (String) -> Unit,
    monthlyTopUp: String,
    onMonthlyTopUpChange: (String) -> Unit,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = interestRate,
            onValueChange = onInterestRateChange,
            label = { Text("Annual Interest Rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = monthlyTopUp,
            onValueChange = onMonthlyTopUpChange,
            label = { Text("Monthly Top Up (₽) - optional") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onCalculate,
                modifier = Modifier.weight(1f),
                enabled = interestRate.isNotBlank()
            ) {
                Text("Calculate")
            }
        }
    }
}

@Composable
fun Step3Result(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double?,
    result: DepositResult?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    val format = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Calculation Result",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Divider()

                ResultRow("Initial Amount:", format.format(initialAmount))
                ResultRow("Period:", "$periodMonths months")
                ResultRow("Interest Rate:", "$interestRate%")
                monthlyTopUp?.let {
                    ResultRow("Monthly Top Up:", format.format(it))
                }

                Divider()

                ResultRow(
                    "Final Amount:",
                    format.format(result?.finalAmount ?: 0.0),
                    isBold = true
                )
                ResultRow(
                    "Interest Earned:",
                    format.format(result?.interestEarned ?: 0.0),
                    isBold = true
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
