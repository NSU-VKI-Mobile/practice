package ci.nsu.mobile.main.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val final = viewModel.calculateFinal()
    val earned = viewModel.calculateEarned()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Результат",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {

                Text("Старт: ${viewModel.startAmount}")
                Text("Срок: ${viewModel.months} мес.")
                Text("Ставка: ${viewModel.percent}%")
                Text("Пополнение: ${viewModel.topUp}")

                Spacer(Modifier.height(12.dp))

                Divider()

                Spacer(Modifier.height(12.dp))

                Text("Начислено: $earned")
                Text("Итого: $final")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveCalculation(final, earned)
                navController.navigate("history")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("main")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}