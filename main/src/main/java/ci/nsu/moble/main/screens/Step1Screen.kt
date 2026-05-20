package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(nav: NavController, vm: DepositViewModel) {

    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Шаг 1") }) }) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = state.initialAmount,
                onValueChange = { if (it.all { char -> char.isDigit() }) {
                    vm.setInitial(it)
                } },
                label = { Text("Стартовый взнос") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.months,
                onValueChange = { if (it.all { char -> char.isDigit() }) {
                    vm.setMonths(it)} },
                label = { Text("Срок (мес)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(8.dp))

            state.error?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    onClick = { nav.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {

                        val amount = state.initialAmount.toDoubleOrNull()
                        val months = state.months.toIntOrNull()

                        when {
                            amount == null || amount <= 0 -> vm.setError("Некорректный взнос")

                            months == null || months <= 0 -> vm.setError("Некорректный срок")

                            else -> {
                                vm.setError(null)
                                vm.calculateRate()
                                nav.navigate("step2")
                            }
                        }
                    }, modifier = Modifier.weight(1f)
                ) {
                    Text("Далее")
                }
            }
        }
    }
}