package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@Composable
fun Step1Screen(navController: NavController, vm: DepositViewModel) {

    val amount by vm.initialAmount.collectAsState()
    val months by vm.months.collectAsState()

    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = amount,
            onValueChange = {
                vm.setInitialAmount(it)
                error = ""
            },
            label = { Text("Стартовый взнос") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = months,
            onValueChange = {
                vm.setMonths(it)
                error = ""
            },
            label = { Text("Срок (месяцы)") }
        )

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(onClick = { navController.navigate("main") }) {
                Text("В начало")
            }

            Button(onClick = {
                when {
                    amount.isEmpty() -> error = "Введите сумму"
                    amount.toDoubleOrNull() == null -> error = "Сумма должна быть числом"
                    amount.toDouble() <= 0 -> error = "Сумма > 0"

                    months.isEmpty() -> error = "Введите срок"
                    months.toIntOrNull() == null -> error = "Срок должен быть числом"
                    months.toInt() <= 0 -> error = "Срок > 0"

                    else -> {
                        vm.calculateRate()
                        navController.navigate("step2")
                    }
                }
            }) {
                Text("Далее")
            }
        }
    }
}