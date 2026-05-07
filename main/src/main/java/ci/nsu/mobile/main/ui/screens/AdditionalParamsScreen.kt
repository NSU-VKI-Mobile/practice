package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AdditionalParamsScreen(
    onNavigateBack: () -> Unit,
    onNavigateResult: (amount: Double, months: Int, rate: Double, topUp: Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var topUpValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val monthsInt = months.toIntOrNull() ?: 0
    val availableRates = when {
        monthsInt < 6 -> listOf(15.0)
        monthsInt < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    var selectedRate by remember { mutableStateOf(availableRates.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Параметры вклада",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Сумма вклада (₽)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = months,
            onValueChange = { months = it },
            label = { Text("Срок (месяцы)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Доступная ставка: ${availableRates.first()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (availableRates.size == 1 && monthsInt > 0) {
                    Text(
                        text = when {
                            monthsInt < 6 -> "Для сроков до 6 месяцев"
                            monthsInt < 12 -> "Для сроков 6-11 месяцев"
                            else -> "Для сроков от 12 месяцев"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (availableRates.size > 1) {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Процентная ставка: ${selectedRate}%")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("${rate}%") },
                            onClick = {
                                selectedRate = rate
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = topUpValue,
            onValueChange = { topUpValue = it },
            label = { Text("Ежемесячное пополнение (₽)") },
            placeholder = { Text("Необязательно") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    val amountNum = amount.toDoubleOrNull() ?: 0.0
                    val monthsNum = months.toIntOrNull() ?: 0
                    val topUpNum = topUpValue.toDoubleOrNull() ?: 0.0
                    if (amountNum > 0 && monthsNum > 0) {
                        onNavigateResult(amountNum, monthsNum, selectedRate, topUpNum)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}