package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val history by viewModel.history.collectAsStateWithLifecycle()
    var selectedCalculation by remember { mutableStateOf<DepositCalculation?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (history.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Нет сохранённых расчётов")
                }
            } else {
                LazyColumn {
                    items(history) { calculation ->
                        CalculationItem(
                            calculation = calculation,
                            onClick = { selectedCalculation = calculation },
                            onDelete = { viewModel.deleteCalculation(calculation) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В начало")
            }
        }
    }

    // Диалог с деталями расчёта
    selectedCalculation?.let { calc ->
        AlertDialog(
            onDismissRequest = { selectedCalculation = null },
            title = { Text("Детали расчёта") },
            text = {
                Column {
                    Text("💰 Стартовый взнос: ${calc.initialAmount} ₽")
                    Text("📅 Срок: ${calc.periodMonths} мес.")
                    Text("📈 Ставка: ${calc.interestRate}%")
                    Text("💸 Пополнение: ${calc.monthlyTopUp ?: "не указано"} ₽")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("🏦 Итоговая сумма: ${String.format("%.2f", calc.finalAmount)} ₽")
                    Text("✨ Начисленные проценты: ${String.format("%.2f", calc.interestEarned)} ₽")
                    Text("📅 Дата: ${formatDate(calc.calculationDate)}")
                }
            },
            confirmButton = {
                Button(onClick = { selectedCalculation = null }) {
                    Text("Закрыть")
                }
            }
        )
    }
}

@Composable
fun CalculationItem(
    calculation: DepositCalculation,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("💰 ${calculation.initialAmount} ₽ → ${String.format("%.2f", calculation.finalAmount)} ₽")
                Text("📅 ${formatDate(calculation.calculationDate)}")
            }

            // Кнопка удаления
            Text(
                text = "🗑️",
                modifier = Modifier
                    .clickable { onDelete() }
                    .padding(8.dp),
                color = Color.Red
            )
        }
    }
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}