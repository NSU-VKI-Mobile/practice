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
fun Input2Screen(
    onBackClick: () -> Unit,
    onCalcClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        OutlinedTextField(
            value =  uiState.interestRate.toString(),
            onValueChange = { newText -> viewModel.SetInterestRate(newText) },
            label = { Text("Введите процент вклада") }
        )
        OutlinedTextField(
            value = uiState.monthlyTopUp.toString(),
            onValueChange = { newText -> viewModel.SetMonthlyTopUp(newText) },
            label = { Text("Введите сумму ежемесячного пополнения (необязательное)") }
        )

        Button(onClick = onBackClick) {
            Text("Назад")
        }
        Button(onClick = onCalcClick) {
            Text("Рассчитать")
        }
    }
}