package ci.nsu.moble.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.moble.main.presentation.viewmodel.CalculationViewModel
import java.text.NumberFormat
import androidx.compose.ui.platform.LocalContext

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: CalculationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currencyFormat = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineMedium
        )

        // Карточка с результатами
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultRow("Стартовый взнос:", currencyFormat.format(uiState.initialAmount.toDoubleOrNull() ?: 0.0))
                ResultRow("Срок вклада:", "${uiState.periodMonths} мес.")
                ResultRow("Процентная ставка:", "${(uiState.interestRate * 100).toInt()}%")

                if (uiState.monthlyTopUp.isNotEmpty()) {
                    ResultRow("Ежемесячное пополнение:", currencyFormat.format(uiState.monthlyTopUp.toDoubleOrNull() ?: 0.0))
                }

                Divider()

                ResultRow("Начисленные проценты:", currencyFormat.format(uiState.interestEarned), isBold = true)
                ResultRow("Итоговая сумма:", currencyFormat.format(uiState.finalAmount), isBold = true, isLarge = true)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопки
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
            Button(
                onClick = {
                    // 1. Сохраняем в базу данных
                    viewModel.saveToDatabase()

                    // 2. Показываем уведомление
                    android.widget.Toast.makeText(
                        context,  // ← используем контекст, полученный выше
                        "Сохранено в историю!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                    // 3. Возвращаемся на главный экран
                    navController.navigate("main")
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Сохранить")
            }
        }
    }
}

// Вспомогательная функция для красивого отображения строк результата
@Composable
private fun ResultRow(label: String, value: String, isBold: Boolean = false, isLarge: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isLarge) MaterialTheme.typography.titleLarge else if (isBold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
        )
    }
}