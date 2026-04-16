package com.example.depositcalculator.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.depositcalculator.Deposit
import com.example.depositcalculator.DepositEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    history: List<DepositEntity>,
    onBack: () -> Unit
) {
    var selectedDeposit by remember { mutableStateOf<DepositEntity?>(null) }
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(title = {Text("История расчётов")})
        }
    ) {
        padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()){
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(history) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {selectedDeposit = item}
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Дата: ${dateFormat.format(Date(item.calculationDate))}")
                            Text("Взнос: ${item.initialAmount}", style = MaterialTheme.typography.bodyLarge)
                            Text("Итого: ${String.format("%.2f", item.finalAmount)}", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("В начало")
            }
        }
    }

    selectedDeposit?.let { deposit ->
        AlertDialog(
            onDismissRequest = { selectedDeposit = null },
            confirmButton = { TextButton(onClick = { selectedDeposit = null}) { Text("ОК") }},
            title = { Text("Детали расчета") },
            text = {
                Column {
                    Text("Ставка: ${deposit.interestRate}%")
                    Text("Срок: ${deposit.periodMonths} мес.")
                    Text("Пополнение: ${deposit.monthlyTopUp ?: 0.0}")
                    Text("Проценты: ${String.format("%.2f", deposit.interestEarned)}")
                }
            }
        )
    }
}