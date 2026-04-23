package com.example.depositcalculator.presentation.result

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.depositcalculator.data.local.DepositCalculationEntity
import com.example.depositcalculator.domain.usecase.CalculateDepositUseCase
import com.example.depositcalculator.presentation.SharedDepositViewModel
import com.example.depositcalculator.presentation.navigation.Screen

@SuppressLint("DefaultLocale")
@Composable
fun ResultScreen(
    navController: NavController,
    vm: SharedDepositViewModel,
    resultVm: ResultViewModel = hiltViewModel()
) {
    val initial = vm.initial.collectAsStateWithLifecycle().value
    val months = vm.months.collectAsStateWithLifecycle().value
    val rate = vm.rate.collectAsStateWithLifecycle().value
    val topUp = vm.topUp.collectAsStateWithLifecycle().value

    val (finalAmount, interest) = CalculateDepositUseCase()(initial, months, rate, topUp)
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.headlineSmall)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Стартовый взнос: $initial ₽")
                Text("Срок: $months мес.")
                Text("Процентная ставка: $rate%")
                if (topUp > 0) Text("Пополнение: $topUp ₽")
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                Text("Итог: ${String.format("%.2f", finalAmount)} ₽")
                Text("Проценты: ${String.format("%.2f", interest)} ₽")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val entity = DepositCalculationEntity(
                    initialAmount = initial,
                    periodMonths = months,
                    interestRate = rate,
                    monthlyTopUp = if (topUp > 0) topUp else null,
                    finalAmount = finalAmount,
                    interestEarned = interest
                )
                resultVm.saveCalculation(entity) {
                    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
                    vm.clear()
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            }) {
                Text("Сохранить")
            }
            Button(onClick = {
                vm.clear()
                navController.popBackStack(Screen.Main.route, inclusive = false)
            }) {
                Text("В начало")
            }
        }
    }
}