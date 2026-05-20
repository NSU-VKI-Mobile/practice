package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositEntity
import ci.nsu.moble.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositDetailsScreen(
    navController: NavController,
    vm: DepositViewModel,
    item: DepositEntity
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали вклада") }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        text = "Основные параметры",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    DetailRow("ID", item.id.toString())
                    DetailRow("Стартовый взнос", item.initialAmount.toString())
                    DetailRow("Срок", "${item.periodMonths} мес")
                    DetailRow("Ставка", "${item.interestRate}%")

                    item.monthlyTopUp?.let {
                        DetailRow("Пополнение", it.toString())
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        text = "Результат",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    DetailRow("Итоговая сумма", "%.2f".format(item.finalAmount))
                    DetailRow("Доход", "%.2f".format(item.interestEarned))
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
            Button(
                onClick = {
                    vm.deleteDeposit(item)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Удалить запись")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value)
    }
}