package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun HistoryScreen(navController: NavController, vm: DepositViewModel) {
    val history by vm.history.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn {
            items(history) { item ->
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Сумма: ${item.amount}")
                        Text("Итог: ${item.finalAmount}")
                    }

                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("В начало")
        }
    }
}