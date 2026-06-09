package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen(viewModel: DepositViewModel) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }


    val rateOptions = listOf("5", "10", "15", "20", "25")
    var selectedRate by remember { mutableStateOf("10") }
    var expandedRate by remember { mutableStateOf(false) }

    var topUp by remember { mutableStateOf("") }
    val result by viewModel.calcResult.collectAsState()


    var amountError by remember { mutableStateOf<String?>(null) }
    var monthsError by remember { mutableStateOf<String?>(null) }


    val canCalculate = amount.isNotBlank() && months.isNotBlank() &&
            amount.toDoubleOrNull() != null && months.toIntOrNull() != null &&
            amount.toDoubleOrNull()!! > 0 && months.toIntOrNull()!! > 0

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("Новый расчет", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                if (it.toDoubleOrNull() == null && it.isNotBlank()) {
                    amountError = "Только числа"
                } else if ((it.toDoubleOrNull() ?: 0.0) <= 0) {
                    amountError = "Сумма должна быть > 0"
                } else {
                    amountError = null
                }
            },
            label = { Text("Сумма") },
            modifier = Modifier.fillMaxWidth(),
            isError = amountError != null,
            supportingText = { amountError?.let { Text(it) } },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = months,
            onValueChange = {
                months = it
                if (it.toIntOrNull() == null && it.isNotBlank()) {
                    monthsError = "Только целые числа"
                } else if ((it.toIntOrNull() ?: 0) <= 0) {
                    monthsError = "Срок должен быть > 0"
                } else {
                    monthsError = null
                }
            },
            label = { Text("Срок (мес)") },
            modifier = Modifier.fillMaxWidth(),
            isError = monthsError != null,
            supportingText = { monthsError?.let { Text(it) } },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))


        ExposedDropdownMenuBox(
            expanded = expandedRate,
            onExpandedChange = { expandedRate = !expandedRate }
        ) {
            OutlinedTextField(
                value = selectedRate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ставка %") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRate) },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedRate,
                onDismissRequest = { expandedRate = false }
            ) {
                rateOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("$option%") },
                        onClick = {
                            selectedRate = option
                            expandedRate = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = topUp, onValueChange = { topUp = it }, label = { Text("Пополнение") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                amountError = null
                monthsError = null

                viewModel.calculateDeposit(
                    amount.toDoubleOrNull() ?: 0.0,
                    months.toIntOrNull() ?: 0,
                    selectedRate.toDouble(),
                    topUp.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canCalculate
        ) {
            Text("Рассчитать")
        }


        result?.let { calc ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Итог: ${String.format("%.2f", calc.finalAmount)}", style = MaterialTheme.typography.titleLarge)
                    Text("Прибыль: ${String.format("%.2f", calc.interestEarned)}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.saveCalculation(calc) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}