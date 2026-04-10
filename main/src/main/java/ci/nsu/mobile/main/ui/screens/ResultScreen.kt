package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.presentation.viewmodel.ResultUiState
import ci.nsu.mobile.main.presentation.viewmodel.ResultViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ResultScreen(
    amount: Double,
    months: Int,
    topUp: Double,
    rate: Double,
    onNavigateHome: () -> Unit
) {
    val viewModel: ResultViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    // Автоматически запускаем расчёт при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.calculateAndSave(amount, months, rate, topUp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.headlineMedium)

        when (uiState) {
            is ResultUiState.Loading -> {
                CircularProgressIndicator()
                Text("Считаем проценты...")
            }

            is ResultUiState.Success -> {
                val calc = (uiState as ResultUiState.Success).calculation

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // === Раздел 1: Входные данные ===
                        Text(
                            text = "📥 Входные параметры",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Стартовый взнос: ${amount} ₽")
                        Text("Срок вклада: $months мес.")
                        Text("Процентная ставка: $rate%")
                        Text("Ежемес. пополнение: $topUp ₽")

                        // --- Разделитель ---
                        Divider(modifier = Modifier.padding(vertical = 16.dp))

                        // === Раздел 2: Итоги расчёта ===
                        Text(
                            text = "📈 Итоги расчёта",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Главная цифра
                        Text(
                            text = "Итоговая сумма: ${"%.2f".format(calc.finalAmount)} ₽",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Прибыль
                        Text(
                            text = "Начисленные проценты: ${"%.2f".format(calc.interestEarned)} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()

                        Text("Дата расчёта: ${dateFormat.format(Date(calc.calculationDate))}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Кнопки управления под карточкой
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✅ Расчёт сохранён в историю")
                }

                Button(onClick = onNavigateHome, modifier = Modifier.fillMaxWidth()) {
                    Text("В начало")
                }
            }

            is ResultUiState.Error -> {
                Text("Ошибка: ${(uiState as ResultUiState.Error).message}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateHome) { Text("На главную") }
            }

            ResultUiState.Idle -> {
                CircularProgressIndicator()
            }
        }
    }
}