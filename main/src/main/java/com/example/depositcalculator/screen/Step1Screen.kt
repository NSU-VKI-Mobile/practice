package com.example.depositcalculator.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Step1Screen(
    onNext: (Double, Int) -> Unit,
    onBack: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var monthsText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = monthsText,
            onValueChange = { monthsText = it },
            label = { Text("Срок (месяцы)") },
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
                Text("В начало")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    val months = monthsText.toIntOrNull()

                    if (amount == null || amount <= 0) {
                        error = "Введите корректный взнос"
                        return@Button
                    }

                    if (months == null || months <= 0) {
                        error = "Введите корректный срок"
                        return@Button
                    }

                    error = ""
                    onNext(amount, months)
                },
                modifier = Modifier.weight(1f)
            ){
                Text("Далее")
            }
        }
    }
}