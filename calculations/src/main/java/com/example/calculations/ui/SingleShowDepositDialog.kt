package com.example.calculations.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculations.R
import com.example.calculations.data.dba.Deposit
import com.example.calculations.vm.DepositsViewModel

@Composable
fun SingleShowDepositDialogScreen(
    onDismiss: () -> Unit,
    thisDeposit: Deposit,
    viewModel: DepositsViewModel = viewModel()
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column() {
                Text(stringResource(R.string.id) + ": " + thisDeposit.id)
                Text(stringResource(R.string.initialAmount) + ": " + thisDeposit.initialAmount)
                Text(stringResource(R.string.periodMonths) + ": " + thisDeposit.periodMonths)
                Text(stringResource(R.string.interestRate) + ": " + thisDeposit.interestRate)
                Text(stringResource(R.string.monthlyTopUp) + ": " + thisDeposit.monthlyTopUp)
                Text(stringResource(R.string.finalAmount) + ": " + thisDeposit.finalAmount)
                Text(stringResource(R.string.interestEarned) + ": " + thisDeposit.interestEarned)
                Text(stringResource(R.string.date) + ": " + viewModel.formatTime(thisDeposit.calculationDate))
                Button(onClick = onDismiss) {
                    Text("Назад")
                }
            }
        }
    }
}
