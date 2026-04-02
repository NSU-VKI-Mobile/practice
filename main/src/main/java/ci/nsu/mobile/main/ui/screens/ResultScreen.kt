package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.DecimalFormat

@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    onSave: () -> Unit,
    onBackToMain: () -> Unit
) {
    val state by viewModel.resultState.collectAsState()
    val decimalFormat = DecimalFormat("#,##0.00")

    // Создаём локальную переменную для monthlyTopUp
    val monthlyTopUpValue = state.monthlyTopUp

    // Если нет результата, возвращаемся
    if (!state.showResult) {
        onBackToMain()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Карточка с результатами
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "📊 Параметры вклада",
                    style = MaterialTheme.typography.titleLarge
                )

                Divider()

                // Стартовый взнос
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("💰 Стартовый взнос:", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "${decimalFormat.format(state.initialAmount)} руб",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Срок вклада
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📅 Срок вклада:", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "${state.periodMonths} месяцев",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Процентная ставка
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📈 Процентная ставка:", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "${String.format("%.1f", state.interestRate)}%",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Ежемесячное пополнение (если указано) - ИСПРАВЛЕНО
                if (monthlyTopUpValue != null && monthlyTopUpValue > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("➕ Ежемесячное пополнение:", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "${decimalFormat.format(monthlyTopUpValue)} руб",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Divider()

                // Итоговая сумма
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "🏦 Итоговая сумма:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${decimalFormat.format(state.finalAmount)} руб",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Начисленные проценты
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "📊 Начисленные проценты:",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "${decimalFormat.format(state.interestEarned)} руб",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("💾 Сохранить")
            }

            Button(
                onClick = onBackToMain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("🏠 В начало")
            }
        }
    }
}