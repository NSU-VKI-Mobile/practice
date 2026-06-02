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

    // 🟢 ВЫПАДАЮЩИЙ СПИСОК ДЛЯ СТАВКИ
    val rateOptions = listOf("5", "10", "15", "20", "25")
    var selectedRate by remember { mutableStateOf("10") }
    var expandedRate by remember { mutableStateOf(false) }

    var topUp by remember { mutableStateOf("") }
    val result by viewModel.calcResult.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("Новый расчет", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Сумма") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = months, onValueChange = { months = it }, label = { Text("Срок (мес)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        // 🟢 DROPDOWN СТАВКИ
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
                modifier = Modifier.menuAnchor().fillMaxWidth()
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

        Button(onClick = {
            viewModel.calculateDeposit(
                amount.toDoubleOrNull() ?: 0.0,
                months.toIntOrNull() ?: 0,
                selectedRate.toDouble(), // 🟢 Берём из выпадающего списка
                topUp.toDoubleOrNull() ?: 0.0
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Рассчитать")
        }

        result?.let { calc ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Итог: ${String.format("%.2f", calc.finalAmount)}")
                    Text("Прибыль: ${String.format("%.2f", calc.interestEarned)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.saveCalculation(calc) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}