package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.navigation.Screen
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun Step1Screen(navController: NavController, vm: DepositViewModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = vm.amount,
            onValueChange = { vm.amount = it },
            label = { Text("Стартовый взнос") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = vm.months,
            onValueChange = { vm.months = it },
            label = { Text("Срок (месяцы)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Row {
            Button(onClick = { navController.popBackStack() }) {
                Text("В начало")
            }

            Button(onClick = {
                if (vm.amount.isNotEmpty() && vm.months.isNotEmpty()) {
                    navController.navigate(Screen.Step2.route)
                }
            }) {
                Text("Далее")
            }
        }
    }
}