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
import ci.nsu.moble.main.presentation.viewmodels.Step1ViewModel

@Composable
fun Step1Screen(
    calcVM: CalculationViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val step1VM: Step1ViewModel = viewModel()
    val state by step1VM.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.amount,
            onValueChange = step1VM::setAmount,
            label = { Text("Стартовый взнос") },
            isError = state.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (state.amountError) Text("Введите положительное число") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.months,
            onValueChange = step1VM::setMonths,
            label = { Text("Срок вклада (мес.)") },
            isError = state.monthsError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (state.monthsError) Text("Введите целое положительное число") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("В начало") }
            Button(
                onClick = {
                    step1VM.getValidatedData()?.let { (amount, months) ->
                        calcVM.setStep1(amount, months)
                        onNext()
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Далее") }
        }
    }
}