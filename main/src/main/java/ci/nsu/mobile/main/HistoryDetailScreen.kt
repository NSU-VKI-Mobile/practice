package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Alignment

@Composable
fun HistoryDetailScreen(
    calculationId: Long,
    onBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Загружаем расчёт по ID
    LaunchedEffect(calculationId) {
        isLoading = true
        calculation = viewModel.getCalculationById(calculationId)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Кнопка "Назад"
        TextButton(
            onClick = onBack,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text("← Назад", fontSize = 16.sp)
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Загрузка...")
            }
        } else if (calculation == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Расчёт не найден")
            }
        } else {
            // Показываем детали расчёта
            val calc = calculation!!
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

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
                    Text(
                        text = "Детали расчёта",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Divider()

                    DetailRow("Дата:", dateFormat.format(Date(calc.calculationDate)))
                    DetailRow("Стартовый взнос:", String.format("%.2f ₽", calc.initialAmount))
                    DetailRow("Срок:", "${calc.periodMonths} мес.")
                    DetailRow("Процентная ставка:", String.format("%.1f %%", calc.interestRate))

                    if (calc.monthlyTopUp > 0) {
                        DetailRow("Ежемесячное пополнение:", String.format("%.2f ₽", calc.monthlyTopUp))
                    }

                    Divider()

                    DetailRow(
                        "Итоговая сумма:",
                        String.format("%.2f ₽", calc.finalAmount),
                        isBold = true,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                    DetailRow(
                        "Начисленные проценты:",
                        String.format("%.2f ₽", calc.interestEarned),
                        isBold = true
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}