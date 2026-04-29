package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.roomDatabase.DepositEntity
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun HistoryScreen(navController: NavController, vm: DepositViewModel) {

    val history by vm.history.collectAsState()

    var selectedItem by remember { mutableStateOf<DepositEntity?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(history) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable {
                            selectedItem = item
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Сумма: ${vm.formatDouble(item.amount)}")
                        Text("Итог: ${vm.formatDouble(item.finalAmount)}")
                    }
                }
            }
        }

        Button(
            onClick = { vm.clearAll() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Очистить историю")
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("В начало")
        }
    }

    selectedItem?.let { item ->

        AlertDialog(
            onDismissRequest = { selectedItem = null },
            confirmButton = {
                Button(onClick = { selectedItem = null }) {
                    Text("ОК")
                }
            },
            title = {
                Text("Детали вклада")
            },
            text = {
                Column {
                    Text("Сумма: ${vm.formatDouble(item.amount)}")
                    Text("Срок: ${item.months} мес.")
                    Text("Ставка: ${item.rate}%")
                    Text("Пополнение: ${item.monthlyTopUp}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Итог: ${vm.formatDouble(item.finalAmount)}")
                    Text("Прибыль: ${vm.formatDouble(item.profit)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Дата: ${vm.formatDate(item.date)}")
                }
            }
        )
    }
}