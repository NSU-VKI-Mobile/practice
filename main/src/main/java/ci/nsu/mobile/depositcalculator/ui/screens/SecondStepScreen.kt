package ci.nsu.mobile.depositcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondStepScreen(
    initialAmount: Double,
    periodMonths: Int,
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Double, Double?) -> Unit
) {
    var selectedRate by remember { mutableStateOf<Double?>(null) }
    var monthlyTopUp by remember { mutableStateOf("") }
    var showWarning by remember { mutableStateOf(false) }

    // Определяем доступные ставки
    val availableRates = when {
        periodMonths < 6 -> listOf(15.0)
        periodMonths < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Параметры вклада - Шаг 2") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Информация о вкладе",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Стартовый взнос: ${String.format("%.2f", initialAmount)} ₽")
                    Text("Срок: $periodMonths месяцев")
                }
            }

            Text(
                text = "Выберите процентную ставку:",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableRates.forEach { rate ->
                    FilterChip(
                        selected = selectedRate == rate,
                        onClick = { selectedRate = rate },
                        label = { Text("$rate%") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            OutlinedTextField(
                value = monthlyTopUp,
                onValueChange = { monthlyTopUp = it },
                label = { Text("Ежемесячное пополнение (₽) - необязательно") },
                placeholder = { Text("Введите сумму") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        if (selectedRate == null) {
                            showWarning = true
                        } else {
                            val topUp = monthlyTopUp.toDoubleOrNull()
                            onNavigateToResult(selectedRate!!, topUp)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }

    if (showWarning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            title = { Text("Внимание") },
            text = { Text("Пожалуйста, выберите процентную ставку для расчёта") },
            confirmButton = {
                TextButton(onClick = { showWarning = false }) {
                    Text("OK")
                }
            }
        )
    }
}
