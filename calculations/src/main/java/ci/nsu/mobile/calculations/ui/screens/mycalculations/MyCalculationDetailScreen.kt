package ci.nsu.mobile.calculations.ui.screens.mycalculations

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.calculations.data.DepositCalculation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MyCalculationDetailScreen(
    calculation: DepositCalculation?,
    isDeleting: Boolean,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val dateStr = remember(calculation?.calculationDate) {
        calculation?.let {
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(Date(it.calculationDate))
        } ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Кнопка назад
        TextButton(
            onClick = onBack,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("← Назад")
        }

        if (calculation == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Расчёт не найден")
            }
            return@Column
        }

        // Карточка с данными
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Дата: $dateStr",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Стартовый взнос: ${calculation.initialAmount} руб.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Срок вклада: ${calculation.periodMonths} месяцев",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Процентная ставка: ${calculation.interestRate}%",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                if (calculation.monthlyTopUp != null && calculation.monthlyTopUp > 0) {
                    Text(
                        text = "Ежемесячное пополнение: ${calculation.monthlyTopUp} руб.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} руб.",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Начисленные проценты: ${String.format("%.2f", calculation.interestEarned)} руб.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // Кнопка удаления
        Button(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            if (isDeleting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Удалить расчёт")
            }
        }
    }
}