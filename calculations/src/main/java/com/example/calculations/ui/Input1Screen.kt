package com.example.calculations.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculations.vm.DepositsViewModel

@Composable
fun Input1Screen(
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
    }
}