package com.example.calculations.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculations.R
import com.example.calculations.vm.DepositsViewModel

@Composable
fun CalcScreen(
    onMainClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel(),
    userId: Long
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.calcFinalAmountAndEarned()
    Column() {
        Text(stringResource(R.string.initialAmount) + ": " + uiState.initialAmount)
        Text(stringResource(R.string.periodMonths) + ": " + uiState.periodMonths)
        Text(stringResource(R.string.interestRate) + ": " + uiState.interestRate)
        Text(stringResource(R.string.monthlyTopUp) + ": " + uiState.monthlyTopUp)
        Text(stringResource(R.string.finalAmount) + ": " + uiState.finalAmount)
        Text(stringResource(R.string.interestEarned) + ": " + uiState.interestEarned)

        Button(onClick = {viewModel.saveDeposit(userId)}, enabled = uiState.isAllCorrect) {
            Text(stringResource(R.string.save))
        }
        Button(onClick = onMainClick) {
            Text(stringResource(R.string.text_back))
        }
    }
}