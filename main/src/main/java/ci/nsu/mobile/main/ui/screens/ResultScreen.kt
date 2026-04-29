package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Вспомогательная функция для красивого вывода валюты
fun formatCurrency(amount: Double): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.groupingSeparator = ' ' // Разделитель групп разрядов — пробел
    symbols.decimalSeparator = ','  // Разделитель дробной части — запятая

    val formatter = DecimalFormat("#,##0.00", symbols)
    return formatter.format(amount)
}

@Composable
fun ResultScreen(navController: NavController, viewModel: DepositViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Результат", style = MaterialTheme.typography.headlineSmall)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Итоговая сумма:",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${formatCurrency(viewModel.finalAmount)} ₽",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Чистый доход (проценты):",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${formatCurrency(viewModel.interestEarned)} ₽",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Button(
            onClick = {
                viewModel.saveToDb()
                navController.navigate("history")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить в историю")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.popBackStack("main", false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}