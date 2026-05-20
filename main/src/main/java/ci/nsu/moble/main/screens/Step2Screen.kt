package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(nav: NavController, vm: DepositViewModel) {

    val state by vm.state.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val months = state.months.toIntOrNull()

    val activeRate = when {
        months == null -> null
        months < 6 -> 15.0
        months in 6..11 -> 10.0
        else -> 5.0
    }

    val allRates = listOf(15.0, 10.0, 5.0)



    Scaffold(
        topBar = { TopAppBar(title = { Text("Шаг 2") }) }
    ) { padding ->

        Column(
            Modifier.padding(padding).padding(16.dp)
        ) {

            if (months == null) {
                Text(
                    text = "Введите срок в первом шаге",
                    color = MaterialTheme.colorScheme.error
                )
            } else {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    OutlinedTextField(
                        value = if (state.rate == 0.0) "Выберите ставку" else "${state.rate}%",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Процентная ставка") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        allRates.forEach { rate ->

                            val isActive = rate == activeRate

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "$rate %",
                                        color = if (isActive)
                                            MaterialTheme.colorScheme.onSurface
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                },
                                enabled = isActive,
                                onClick = {
                                    vm.setRate(rate)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = state.monthly,
                onValueChange = { if (it.all { char -> char.isDigit() }) {
                    vm.setMonthly(it)}  },
                label = { Text("Ежемесячное пополнение") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

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
                        if (state.rate == 0.0) return@Button
                        vm.calculate()
                        nav.navigate("result")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}