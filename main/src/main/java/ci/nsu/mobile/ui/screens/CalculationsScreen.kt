package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.ui.viewmodel.DepositViewModel

@Composable
fun CalculationsScreen(
    vm: DepositViewModel,
    userLogin: String,
    onOpen: (DepositCalculation) -> Unit
) {

    LaunchedEffect(userLogin) {
        vm.load(userLogin)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "История расчётов (ID: $userLogin)",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                vm.clearHistory(userLogin)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Очистить всё")
        }

        Spacer(Modifier.height(16.dp))

        if (vm.history.isEmpty()) {
            Text("История пуста")
            return@Column
        }

        LazyColumn {
            items(vm.history) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { onOpen(item) }
                ) {

                    Column(Modifier.padding(12.dp)) {
                        Text("Сумма: ${item.initialAmount}")
                        Text("Срок: ${item.periodMonths} мес")
                        Text("Итог: ${item.finalAmount}")
                    }
                }
            }
        }
    }
}