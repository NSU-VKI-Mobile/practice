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
fun CalcScreen(
    onMainClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.CalcFinalAmountAndEarned()
    Column() {
        Text("Стартовый взнос: " + uiState.initialAmount)
        Text("Срок вклада: " + uiState.periodMonths)
        Text("Процентная ставка: " + uiState.interestRate)
        Text("Ежемесячное пополнение: " + uiState.monthlyTopUp)
        Text("Итоговая сумма: " + uiState.finalAmount)
        Text("Начисленные проценты: " + uiState.interestEarned)

        Button(onClick = {viewModel.SaveDeposit()}) {
            Text("Сохранить")
        }
        Button(onClick = onMainClick) {
            Text("В начало")
        }
    }
}