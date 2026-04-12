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
            value =  uiState.initialAmount,
            onValueChange = { newText -> viewModel.setInitialAmount(newText) },
            label = { Text("Введите стартовый взнос") },
            isError = !uiState.isInitialAmountValid
        )
        OutlinedTextField(
            value = uiState.periodMonths,
            onValueChange = { newText -> viewModel.setPeriodMonths(newText) },
            label = { Text("Введите срок вклада в месяцах") },
            isError = !uiState.isPeriodMonthsValid
        )
        Button(onClick = onNextClick) {
            Text("Далее")
        }
        Button(onClick = onBackClick) {
            Text("В начало")
        }
    }
}