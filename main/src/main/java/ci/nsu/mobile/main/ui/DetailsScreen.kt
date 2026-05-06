package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun DetailsScreen(
    navController: NavController,
    id: Long
) {

    val viewModel: DepositViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    val list by viewModel.history.observeAsState(emptyList())
    val item = list.find { it.id == id }

    if (item == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Запись не найдена")
        }
        return
    }

    Column(

        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            "Детали расчёта",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ===== КАРТОЧКА =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                InfoRow("Сумма", item.amount.toString())
                InfoRow("Срок", "${item.months} мес")
                InfoRow("Ставка", "${item.rate}%")
                InfoRow("Пополнение", item.topUp.toString())

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    "Итог: %.2f".format(item.result),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ===== КНОПКИ =====
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                viewModel.delete(item)
                navController.popBackStack()
            }
        ) {
            Text("Удалить")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Назад")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}