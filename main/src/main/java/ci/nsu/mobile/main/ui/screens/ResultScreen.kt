package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ViewModel.DepositViewModel

@Composable
fun ResultScreen(navController: NavController, viewModel: DepositViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (viewModel.isReadOnlyMode) "Детали расчёта" else "Результат расчёта",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Divider()

                ResultRow(label = "Стартовый взнос:", value = "${viewModel.initialAmount} ₽")
                ResultRow(label = "Срок вклада:", value = "${viewModel.periodMonths} мес.")
                ResultRow(label = "Ставка:", value = "${viewModel.getAvailableRate()}%")
                ResultRow(label = "Ежемесячное пополнение:", value = "${viewModel.monthlyTopUp} ₽")

                Divider()

                Text(
                    text = "Итоговая сумма:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${"%.2f".format(viewModel.finalAmount)} ₽",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Чистый доход: ${"%.2f".format(viewModel.interestEarned)} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        if (viewModel.isReadOnlyMode) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Вернуться в историю", fontSize = 16.sp)
            }
        } else {
            Button(
                onClick = {
                    viewModel.saveCalculation()
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Сохранить и в начало", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}