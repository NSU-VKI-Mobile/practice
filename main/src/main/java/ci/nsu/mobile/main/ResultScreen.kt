package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double,
    onSave: () -> Unit,
    onBackToMain: () -> Unit
) {
    // Флаг, показывающий, был ли уже сохранён этот расчёт
    var isSaved by remember { mutableStateOf(false) }

    // Вызываем функцию расчёта
    val (finalAmount, interestEarned) = calculateDeposit(
        initialAmount = initialAmount,
        periodMonths = periodMonths,
        annualRate = interestRate,
        monthlyTopUp = monthlyTopUp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Результат расчёта",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow("Стартовый взнос:", String.format("%.2f ₽", initialAmount))
                InfoRow("Срок вклада:", "$periodMonths мес.")
                InfoRow("Процентная ставка:", String.format("%.1f %%", interestRate))

                if (monthlyTopUp > 0) {
                    InfoRow("Ежемесячное пополнение:", String.format("%.2f ₽", monthlyTopUp))
                }

                Divider()

                InfoRow(
                    "Итоговая сумма:",
                    String.format("%.2f ₽", finalAmount),
                    isBold = true,
                    textColor = MaterialTheme.colorScheme.primary
                )
                InfoRow(
                    "Начисленные проценты:",
                    String.format("%.2f ₽", interestEarned),
                    isBold = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Кнопка "Сохранить" - отключается после сохранения
            Button(
                onClick = {
                    if (!isSaved) {
                        isSaved = true  // помечаем как сохранённый
                        onSave()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = !isSaved  // блокируем кнопку после сохранения
            ) {
                Text(if (isSaved) "Сохранено!" else "Сохранить")
            }

            Button(
                onClick = onBackToMain,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
        }

        // Если расчёт уже сохранён, показываем подсказку
        if (isSaved) {
            Text(
                text = "✓ Расчёт уже сохранён в истории",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// InfoRow и calculateDeposit остаются без изменений
@Composable
fun InfoRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

fun calculateDeposit(
    initialAmount: Double,
    periodMonths: Int,
    annualRate: Double,
    monthlyTopUp: Double
): Pair<Double, Double> {
    val monthlyRate = annualRate / 100 / 12
    var total = initialAmount

    for (month in 1..periodMonths) {
        total += total * monthlyRate
        if (monthlyTopUp > 0) {
            total += monthlyTopUp
        }
    }

    val interestEarned = total - initialAmount - (monthlyTopUp * periodMonths)
    return Pair(total, interestEarned)
}