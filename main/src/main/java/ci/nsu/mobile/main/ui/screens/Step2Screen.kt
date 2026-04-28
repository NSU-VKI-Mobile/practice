package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.components.RateDropdown
import ci.nsu.mobile.main.ui.navigation.Screen
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun Step2Screen(navController: NavController, vm: DepositViewModel) {

    val rates = vm.determineRate()



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        if (rates.isEmpty()) {
            Text("Введите срок корректно")
            return
        }

        RateDropdown(
            options = rates,
            onSelected = { selected ->
                vm.rate = selected
            }

        )


        OutlinedTextField(
            value = vm.monthlyTopUp,
            onValueChange = { vm.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение") }
        )

        Row {
            Button(onClick = { navController.popBackStack() }) {
                Text("Назад")
            }

            Button(onClick = {
                vm.calculate()
                navController.navigate(Screen.Result.route)
            }) {
                Text("Рассчитать")
            }
        }
    }
}