package ci.nsu.mobile.main.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val history = viewModel.history.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("История")

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(history) { item ->
                Card {
                    Column(Modifier.padding(8.dp)) {
                        Text("Дата: ${item.calculationDate}")
                        Text("Старт: ${item.initialAmount}")
                        Text("Итог: ${item.finalAmount}")
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate("main")
        }) {
            Text("В начало")
        }
    }
}