package com.example.depositcalculator.screen

import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    months: Int,
    onBack: () -> Unit,
    onCalculate: (Double, Double?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRate by remember { mutableStateOf<Double?>(null) }
    var topUpText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val rates = when {
        months < 6 -> listOf(15.0)
        months < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Шаг 2: Дополнительные параметры", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it}
        ) {
            OutlinedTextField(
                value = selectedRate?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка")},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                rates.forEach { rate ->
                    DropdownMenuItem(
                        text = {Text("$rate %")},
                        onClick = {
                            selectedRate = rate
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = topUpText,
                onValueChange = { topUpText = it },
                label = { Text("Ежемесячное пополнение (необязательно)")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val rate = selectedRate
                        val topUp = topUpText.toDoubleOrNull()

                        if (rate == null) {
                            error = "Выберите ставку"
                            return@Button
                        }

                        error = ""
                        onCalculate(rate, topUp)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}