package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputSecondScreen(
    initialAmount: Double,
    periodMonths: Int,
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Double, Double) -> Unit
) {
    var monthlyTopUp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Определяем доступные ставки в зависимости от срока
    val availableRates = when {
        periodMonths < 6 -> listOf(15.0)
        periodMonths < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    var selectedRate by remember { mutableStateOf(availableRates.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Дополнительные параметры",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Стартовый взнос: ${initialAmount} ₽")
        Text("Срок: ${periodMonths} мес.")

        Spacer(modifier = Modifier.height(24.dp))

        // Выбор процентной ставки
        Text("Процентная ставка (% годовых):")
        Spacer(modifier = Modifier.height(8.dp))

        availableRates.forEach { rate ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = selectedRate == rate,
                    onClick = { selectedRate = rate }
                )
                Text("${rate}%", modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = monthlyTopUp,
            onValueChange = { monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (₽) (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0
                    if (topUp < 0) {
                        errorMessage = "Пополнение не может быть отрицательным"
                    } else {
                        errorMessage = ""
                        onNavigateToResult(topUp, selectedRate)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}