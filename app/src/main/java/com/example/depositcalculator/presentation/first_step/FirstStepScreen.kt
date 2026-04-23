package com.example.depositcalculator.presentation.first_step

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.depositcalculator.presentation.SharedDepositViewModel
import com.example.depositcalculator.presentation.navigation.Screen

@Composable
fun FirstStepScreen(
    navController: NavController,
    vm: SharedDepositViewModel
) {
    var initialStr by remember { mutableStateOf("") }
    var monthsStr by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Основные параметры", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = initialStr,
            onValueChange = { initialStr = it },
            label = { Text("Стартовый взнос") }
        )
        OutlinedTextField(
            value = monthsStr,
            onValueChange = { monthsStr = it },
            label = { Text("Срок (месяцы)") }
        )
        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { navController.popBackStack() }) {
                Text("В начало")
            }
            Button(onClick = {
                val initial = initialStr.toDoubleOrNull()
                val months = monthsStr.toIntOrNull()
                if (initial == null || initial <= 0 || months == null || months <= 0) {
                    error = "Введите положительные числа"
                } else {
                    vm.setInitial(initial)
                    vm.setMonths(months)
                    navController.navigate(Screen.SecondStep.route)
                }
            }) {
                Text("Далее")
            }
        }
    }
}