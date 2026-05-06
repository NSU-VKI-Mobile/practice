package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    amount: String,
    months: String,
    rate: String,
    topUp: String
) {

    val P = amount.toDoubleOrNull() ?: 0.0
    val n = months.toIntOrNull() ?: 0
    val r = (rate.toDoubleOrNull() ?: 0.0) / 100 / 12
    val m = topUp.toDoubleOrNull() ?: 0.0

    val viewModel: DepositViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    var total = P

    repeat(n) {
        total = (total + m) * (1 + r)
    }

    val interest = total - P - (m * n)

    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Результат", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        Text("Итог: %.2f".format(total))
        Text("Проценты: %.2f".format(interest))

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.save(
                    DepositCalculation(
                        amount = P,
                        months = n,
                        rate = rate.toDoubleOrNull() ?: 0.0,
                        topUp = m,
                        result = total,
                        date = System.currentTimeMillis()
                    )
                )
                saved = true
            },
            enabled = !saved
        ) {
            Text(if (saved) "Сохранено" else "Сохранить")
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                navController.popBackStack("main", false)
            }
        ) {
            Text("В начало")
        }
    }
}