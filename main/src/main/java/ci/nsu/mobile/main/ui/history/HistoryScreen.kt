package ci.nsu.mobile.main.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(time: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(time))
}
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val history = viewModel.history.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("История")

        Spacer(Modifier.height(12.dp))

        // список занимает всё доступное место
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(history) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp) // ✔ отступ между плитками
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Дата: ${formatDate(item.calculationDate)}")
                        Text("Старт: ${item.initialAmount}")
                        Text("Итог: ${item.finalAmount}")
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("main")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}