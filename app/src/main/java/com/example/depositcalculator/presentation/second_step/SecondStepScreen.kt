package com.example.depositcalculator.presentation.second_step

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.depositcalculator.presentation.SharedDepositViewModel
import com.example.depositcalculator.presentation.navigation.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SecondStepScreen(
    navController: NavController,
    vm: SharedDepositViewModel
) {
    val months = vm.months.value
    val availableRates = when {
        months < 6 -> listOf(15.0)
        months < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }
    var selectedRate by remember { mutableDoubleStateOf(availableRates[0]) }
    var topUpStr by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Дополнительные параметры", style = MaterialTheme.typography.headlineSmall)
        Text("Срок: $months мес.")

        // Выпадающий список
        Box {
            Button(onClick = { expanded = true }) {
                Text("Ставка: $selectedRate% ▼")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate%") },
                        onClick = {
                            selectedRate = rate
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = topUpStr,
            onValueChange = { topUpStr = it },
            label = { Text("Ежемесячное пополнение (необязательно)") }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Назад")
            }
            Button(onClick = {
                vm.setRate(selectedRate)
                vm.setTopUp(topUpStr.toDoubleOrNull() ?: 0.0)
                navController.navigate(Screen.Result.route)
            }) {
                Text("Рассчитать")
            }
        }
    }
}