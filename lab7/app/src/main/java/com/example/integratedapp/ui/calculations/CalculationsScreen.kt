package com.example.integratedapp.ui.calculations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integratedapp.data.db.DepositCalculation
import com.example.integratedapp.di.ServiceLocator
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private fun formatMoney(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    fmt.minimumFractionDigits = 2; fmt.maximumFractionDigits = 2
    return "${fmt.format(amount)} ₽"
}

private fun formatDate(timestamp: Long): String =
    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru")).format(Date(timestamp))

@Composable
fun CalculationsScreen(
    viewModel: CalculationsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {

        // Фильтры
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.minAmount,
                onValueChange = viewModel::onMinAmountChanged,
                label = { Text("Сумма от") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.maxAmount,
                onValueChange = viewModel::onMaxAmountChanged,
                label = { Text("до") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        if (uiState.calculations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет сохранённых расчётов", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.calculations) { calc ->
                    CalcCard(calc = calc, onClick = { viewModel.selectCalculation(calc) })
                }
            }
        }
    }

    // Диалог с деталями
    uiState.selectedCalculation?.let { calc ->
        AlertDialog(
            onDismissRequest = { viewModel.selectCalculation(null) },
            title = { Text("Детали расчёта") },
            text = {
                Column {
                    Text("Дата: ${formatDate(calc.calculationDate)}")
                    Text("Стартовый взнос: ${formatMoney(calc.initialAmount)}")
                    Text("Срок: ${calc.periodMonths} мес.")
                    Text("Ставка: ${calc.interestRate}%")
                    calc.monthlyTopUp?.let {
                        if (it > 0) Text("Ежем. пополнение: ${formatMoney(it)}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Итог: ${formatMoney(calc.finalAmount)}", fontWeight = FontWeight.Bold)
                    Text("Начислено %: ${formatMoney(calc.interestEarned)}")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(calc)
                }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.selectCalculation(null) }) { Text("Закрыть") }
            }
        )
    }
}

@Composable
private fun CalcCard(calc: DepositCalculation, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatDate(calc.calculationDate), style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${calc.interestRate}% / ${calc.periodMonths} мес.",
                    style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Взнос: ${formatMoney(calc.initialAmount)}", style = MaterialTheme.typography.bodyMedium)
                Text("Итог: ${formatMoney(calc.finalAmount)}", fontWeight = FontWeight.Bold)
            }
        }
    }
}
