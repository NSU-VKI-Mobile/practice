package ci.nsu.mobile.main.ui.screens.deposit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.components.RateDropdown


@Composable
fun Step2Screen(
    vm: DepositViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit


) {

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
            label = { Text("Ежемесячное пополнение") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Row {
            Button(onClick = onBack) {
                Text("Назад")
            }

            Button(onClick = onNext ) {
                Text("Рассчитать")
            }
        }
    }
}