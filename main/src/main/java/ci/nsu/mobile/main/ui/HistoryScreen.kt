package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun HistoryScreen(navController: NavController) {

    val viewModel: DepositViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    val list by viewModel.history.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "История расчётов",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ) {

            items(list) { item ->

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        navController.navigate("details/${item.id}")
                    }
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("Сумма: ${item.amount}")
                        Text("Срок: ${item.months} мес")
                        Text("Ставка: ${item.rate}%")
                        Text("Итог: %.2f".format(item.result))

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "Нажмите для подробностей",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            }
        ) {
            Text("В главное меню")
        }
    }
}