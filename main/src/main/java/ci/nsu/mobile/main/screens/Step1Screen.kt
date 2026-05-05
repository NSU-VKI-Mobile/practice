package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@Composable
fun Step1Screen(navController: NavController, vm: DepositViewModel = viewModel()) {

    val amount by vm.initialAmount.collectAsState()
    val months by vm.months.collectAsState()

    Column {
        TextField(
            value = amount,
            onValueChange = vm::setInitialAmount,
            label = { Text("Стартовый взнос") }
        )

        TextField(
            value = months,
            onValueChange = vm::setMonths,
            label = { Text("Срок (месяцы)") }
        )

        Button(onClick = { navController.navigate("main") }) {
            Text("В начало")
        }

        Button(onClick = {
            vm.calculateRate()
            navController.navigate("step2")
        }) {
            Text("Далее")
        }
    }
}