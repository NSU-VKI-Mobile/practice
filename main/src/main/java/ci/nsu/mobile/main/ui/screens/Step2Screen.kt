package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(viewModel: DepositViewModel, onBack: () -> Unit, onCalculate: () -> Unit) {
    val isPeriodValid = viewModel.periodMonths.toIntOrNull() != null
    val availableRates = viewModel.getAvailableRates()
    var expanded by remember { mutableStateOf(false) }

    // Автоматически выбираем ставку, если она доступна
    LaunchedEffect(availableRates) {
        if (availableRates.isNotEmpty() && viewModel.selectedRate.isEmpty()) {
            viewModel.selectedRate = availableRates.first().toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 2: Дополнительно", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (!isPeriodValid) {
            // Вывод предупреждения, если срок не указан или указан неверно
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text(
                    text = "Предупреждение: Срок вклада не указан. Вернитесь назад и введите корректный срок.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        } else {
            // Выпадающий список
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = if (viewModel.selectedRate.isNotEmpty()) "${viewModel.selectedRate}%" else "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите процентную ставку") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate%") },
                            onClick = {
                                viewModel.selectedRate = rate.toString()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (необязательно)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = isPeriodValid // Блокируем поле, если срок неверен
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("Назад") }
            Button(
                onClick = {
                    viewModel.calculateDeposit()
                    onCalculate()
                },
                enabled = isPeriodValid && viewModel.selectedRate.isNotEmpty()
            ) { Text("Рассчитать") }
        }
    }
}