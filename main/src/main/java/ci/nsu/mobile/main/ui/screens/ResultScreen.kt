package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val initialAmount by viewModel::initialAmount
    val periodMonths by viewModel::periodMonths
    val selectedRate by viewModel::selectedRate
    val monthlyTopUp by viewModel::monthlyTopUp
    val calculationResult by viewModel::calculationResult

    // Защита от null — если расчёта нет, показываем ошибку
    val finalAmount = calculationResult?.first ?: 0.0
    val interestEarned = calculationResult?.second ?: 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчёта") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("💰 Стартовый взнос: ${initialAmount.toDoubleOrNull() ?: 0} ₽")
                    Text("📅 Срок вклада: ${periodMonths.toIntOrNull() ?: 0} мес.")
                    Text("📈 Процентная ставка: $selectedRate%")
                    Text("💸 Ежемесячное пополнение: ${monthlyTopUp.toDoubleOrNull() ?: 0} ₽")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🏦 Итоговая сумма: ${String.format("%.2f", finalAmount)} ₽")
                    Text("✨ Начисленные проценты: ${String.format("%.2f", interestEarned)} ₽")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.saveCalculation {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.reset()
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В начало")
            }
        }
    }
}