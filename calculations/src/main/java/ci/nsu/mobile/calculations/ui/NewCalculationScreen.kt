package ci.nsu.mobile.calculations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewCalculationScreen(viewModel: DepositViewModel) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
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
        OutlinedTextField(value = rate, onValueChange = { rate = it }, label = { Text("Ставка %") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = topUp, onValueChange = { topUp = it }, label = { Text("Пополнение") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.calculateDeposit(
                amount.toDoubleOrNull() ?: 0.0,
                months.toIntOrNull() ?: 0,
                rate.toDoubleOrNull() ?: 0.0,
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