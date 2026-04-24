package ci.nsu.moble.main.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.presentation.viewmodels.CalculationViewModel
import ci.nsu.moble.main.presentation.viewmodels.Step2ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    calcVM: CalculationViewModel,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val step2VM: Step2ViewModel = viewModel()
    val calcState by calcVM.state.collectAsState()
    val state by step2VM.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(calcState.periodMonths) {
        step2VM.setPeriod(calcState.periodMonths)
    }

    val isValid = state.interestRate != null && !state.topUpError

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.showRateWarning) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text("Срок не указан или некорректен. Вернитесь к первому шагу.", modifier = Modifier.padding(16.dp))
            }
        }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            TextField(
                value = state.interestRate?.let { "$it %" } ?: "Выберите ставку",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                enabled = !state.showRateWarning && state.availableRates.isNotEmpty()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                state.availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate %") },
                        onClick = { step2VM.setRate(rate); expanded = false }
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.monthlyTopUp,
            onValueChange = step2VM::setTopUp,
            label = { Text("Ежемесячное пополнение (необязательно)") },
            isError = state.topUpError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Назад") }
            Button(
                enabled = isValid,
                onClick = {
                    step2VM.getValidatedData()?.let { (rate, topUp) ->
                        calcVM.setStep2(rate, topUp)
                        onCalculate()
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Рассчитать") }
        }
    }
}