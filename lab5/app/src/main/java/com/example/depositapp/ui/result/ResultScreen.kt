package com.example.depositapp.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.depositapp.ui.DepositViewModel
import java.text.NumberFormat
import java.util.Locale

// Форматирование числа как денежной суммы: 150000.5 → "150 000,50 ₽"
private fun formatMoney(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    fmt.minimumFractionDigits = 2
    fmt.maximumFractionDigits = 2
    return "${fmt.format(amount)} ₽"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    onSave: () -> Unit,
    onHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Результат расчёта") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Карточка с результатами
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Параметры вклада",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Строка параметра: название и значение
                    ResultRow("Стартовый взнос", formatMoney(uiState.initialAmountText.toDoubleOrNull() ?: 0.0))
                    ResultRow("Срок вклада", "${uiState.periodMonthsText} мес.")
                    ResultRow("Процентная ставка", "${uiState.selectedRate}% годовых")

                    // Пополнение показываем только если оно указано
                    val topUp = uiState.monthlyTopUpText.toDoubleOrNull()
                    if (topUp != null && topUp > 0) {
                        ResultRow("Ежем. пополнение", formatMoney(topUp))
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Итоги выделяем визуально
                    ResultRow(
                        label = "Итоговая сумма",
                        value = formatMoney(uiState.finalAmount),
                        highlight = true
                    )
                    ResultRow(
                        label = "Начисленные %",
                        value = formatMoney(uiState.interestEarned),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Сообщение об успешном сохранении
            if (uiState.isSaved) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Расчёт сохранён в историю",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Сообщение об ошибке сохранения
            uiState.saveError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Кнопка "Сохранить" — недоступна после сохранения
                OutlinedButton(
                    onClick = onSave,
                    enabled = !uiState.isSaved,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text(if (uiState.isSaved) "Сохранено" else "Сохранить")
                }

                Button(
                    onClick = onHome,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("В начало")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Вспомогательный composable — строка "Название: Значение"
@Composable
private fun ResultRow(
    label: String,
    value: String,
    highlight: Boolean = false,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (highlight) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (highlight) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) MaterialTheme.colorScheme.onSurface else valueColor
        )
    }
}
