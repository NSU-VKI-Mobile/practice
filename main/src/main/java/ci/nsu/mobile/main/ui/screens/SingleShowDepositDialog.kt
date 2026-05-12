package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.entity.Deposit
import ci.nsu.mobile.main.vm.DepositsViewModel

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
                Text("Id: " + thisDeposit.id)
                Text("Стартовый взнос: " + thisDeposit.initialAmount)
                Text("Период(в месяцах): " + thisDeposit.periodMonths)
                Text("Процентная ставка: " + thisDeposit.interestRate)
                Text("Ежемесячный взнос: " + thisDeposit.monthlyTopUp)
                Text("Итоговая сумма: " + thisDeposit.finalAmount)
                Text("Начисленные проценты: " + thisDeposit.interestEarned)
                Text("Дата: " + viewModel.formatTime(thisDeposit.calculationDate))
                Button(onClick = onDismiss) {
                    Text("Назад")
                }
            }
        }
    }
}
