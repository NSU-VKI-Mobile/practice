package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.vm.DepositsViewModel

@Composable
fun Input1Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        OutlinedTextField(
            value =  uiState.initialAmount.toString(),
            onValueChange = { newText -> viewModel.SetInitialAmount(newText) },
            label = { Text("Введите стартовый взнос") }
        )
        OutlinedTextField(
            value = uiState.periodMonths.toString(),
            onValueChange = { newText -> viewModel.SetPeriodMonths(newText) },
            label = { Text("Введите срок вклада в месяцах") }
        )

        Button(onClick = onBackClick) {
            Text("В начало")
        }
        Button(onClick = onNextClick) {
            Text("Далее")
        }
    }
}