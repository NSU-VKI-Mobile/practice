package com.example.depositcalculator.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.depositcalculator.Deposit


@Composable
fun ResultScreen(
    deposit: Deposit,
    onBackToMain: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${deposit.initialAmount}")
                Text("Срок: ${deposit.periodMonths} месяцев")
                Text("Процентная ставка: ${deposit.interestRate} %")
                Text("Ежемесячное пополнение: ${deposit.monthlyTopUp ?: 0.0}")
                Text("Начисленные проценты: ${deposit.interestEarned}")
                Text("Итоговая сумма: ${deposit.finalAmount}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onBackToMain,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
        }
    }
}