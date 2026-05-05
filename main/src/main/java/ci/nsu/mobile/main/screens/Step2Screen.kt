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
fun Step2Screen(navController: NavController, vm: DepositViewModel = viewModel()) {

    val rate by vm.rate.collectAsState()
    val topUp by vm.topUp.collectAsState()

    Column {
        Text("Ставка: $rate %")

        TextField(
            value = topUp,
            onValueChange = vm::setTopUp,
            label = { Text("Пополнение") }
        )

        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }

        Button(onClick = {
            navController.navigate("result")
        }) {
            Text("Рассчитать")
        }
    }
}