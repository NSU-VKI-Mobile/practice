package ci.nsu.mobile.calculations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen(viewModel: DepositViewModel) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var topUp by remember { mutableStateOf("") }

    val result by viewModel.calcResult.collectAsState()

    // 🟢 ВСЕГДА ДОСТУПНЫЕ СТАВКИ (список не блокируется)
    val allRates = listOf(5, 10, 15)
    var selectedRate by remember { mutableStateOf<Int?>(10) } // По умолчанию 10%

    // Вспомогательная функция: минимальный срок для ставки
    fun minMonthsForRate(rate: Int): Int = when (rate) {
        15 -> 1   // 15%: 1–5 мес
        10 -> 6   // 10%: 6–11 мес
        5 -> 12   // 5%: от 12 мес
        else -> 1
    }

    // 🟢 ОБРАТНАЯ СВЯЗЬ: при выборе ставки корректируем срок
    fun onRateSelected(rate: Int) {
        selectedRate = rate
        val minMonths = minMonthsForRate(rate)
        // Если текущий срок меньше минимального — автоматически ставим минимум
        val currentMonths = months.toIntOrNull() ?: 0
        if (currentMonths < minMonths) {
            months = minMonths.toString()
        }
    }

    // 🟢 ПРЯМАЯ СВЯЗЬ: при вводе срока обновляем ставку (подсказка)
    fun onMonthsChanged(newMonths: String) {
        months = newMonths
        val monthsInt = newMonths.toIntOrNull() ?: 0
        // Автоматически подсказываем ставку, но не блокируем выбор пользователя
        if (monthsInt > 0 && selectedRate != null) {
            val suggestedRate = when {
                monthsInt < 6 -> 15
                monthsInt < 12 -> 10
                else -> 5
            }
            // Если текущая ставка не соответствует сроку — показываем подсказку (но не меняем насильно)
            if (selectedRate != suggestedRate) {
                // Можно добавить Snackbar или просто оставить выбор за пользователем
            }
        }
    }

    // Валидация
    var amountError by remember { mutableStateOf<String?>(null) }

    val canCalculate = amount.isNotBlank() &&
            months.isNotBlank() &&
            months.toIntOrNull() != null &&
            months.toIntOrNull()!! > 0 &&
            selectedRate != null &&
            amount.toDoubleOrNull() != null &&
            amount.toDoubleOrNull()!! > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Новый расчет", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Сумма
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                amountError = when {
                    it.isBlank() -> "Обязательное поле"
                    it.toDoubleOrNull() == null -> "Только числа"
                    it.toDoubleOrNull()!! <= 0 -> "Сумма > 0"
                    else -> null
                }
            },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth(),
            isError = amountError != null,
            supportingText = { amountError?.let { Text(it) } },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Срок
        OutlinedTextField(
            value = months,
            onValueChange = { onMonthsChanged(it) },
            label = { Text("Срок вклада (мес)") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                // 🟢 ПОДСКАЗКА: какой срок нужен для текущей ставки
                selectedRate?.let { rate ->
                    val minMonths = minMonthsForRate(rate)
                    val maxMonths = when (rate) {
                        15 -> "5"
                        10 -> "11"
                        5 -> "∞"
                        else -> ""
                    }
                    Text("Для ставки $rate%: срок $minMonths–$maxMonths мес",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 🟢 ВЫПАДАЮЩИЙ СПИСОК СТАВКИ (ВСЕГДА АКТИВЕН!)
        var expandedRate by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedRate,
            onExpandedChange = { expandedRate = !expandedRate },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedRate?.let { "$it%" } ?: "Выберите ставку",
                onValueChange = {},
                readOnly = true,
                // 🟢 УБРАНО: enabled = ... (теперь всегда активно!)
                label = { Text("Процентная ставка") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRate)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            // 🟢 МЕНЮ СО ВСЕМИ СТАВКАМИ
            ExposedDropdownMenu(
                expanded = expandedRate,
                onDismissRequest = { expandedRate = false }
            ) {
                allRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate%") },
                        onClick = {
                            onRateSelected(rate)
                            expandedRate = false
                        },
                        // Подсветка выбранной ставки
                        leadingIcon = {
                            if (rate == selectedRate) {
                                Icon(
                                    androidx.compose.material.icons.Icons.Default.Check,
                                    contentDescription = "Выбрано",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            }
        }

        // 🟢 ПРЕДУПРЕЖДЕНИЕ, ЕСЛИ СРОК НЕ СООТВЕТСТВУЕТ ВЫБРАННОЙ СТАВКЕ
        val monthsInt = months.toIntOrNull() ?: 0
        if (monthsInt > 0 && selectedRate != null) {
            val minMonths = minMonthsForRate(selectedRate!!)
            if (monthsInt < minMonths) {
                Text(
                    "⚠️ Для ставки ${selectedRate}% минимальный срок: $minMonths мес",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Пополнение
        OutlinedTextField(
            value = topUp,
            onValueChange = { topUp = it },
            label = { Text("Ежемесячное пополнение (необязательно)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 🟢 КНОПКА С БЛОКИРОВКОЙ (только если ввод некорректен)
        Button(
            onClick = {
                viewModel.calculateDeposit(
                    amount.toDoubleOrNull() ?: 0.0,
                    months.toIntOrNull() ?: 0,
                    selectedRate?.toDouble() ?: 0.0,
                    topUp.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canCalculate
        ) {
            Text("Рассчитать")
        }

        // 🟢 РЕЗУЛЬТАТ
        result?.let { calc ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Итог: ${String.format("%.2f", calc.finalAmount)}",
                        style = MaterialTheme.typography.titleLarge)
                    Text("Прибыль: ${String.format("%.2f", calc.interestEarned)}")
                    Text("Ставка: ${calc.interestRate}%")
                    Text("Срок: ${calc.periodMonths} мес.")
                    if (calc.monthlyTopUp > 0) {
                        Text("Пополнение: ${calc.monthlyTopUp}/мес")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.saveCalculation(calc) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}