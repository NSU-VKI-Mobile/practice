package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.ui.viewmodel.DepositViewModel

@Composable
fun CalculationDetailsScreen(
    item: DepositCalculation,
    vm: DepositViewModel,
    userLogin: String,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Детали расчёта", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        Text("Сумма: ${item.initialAmount}")
        Text("Срок: ${item.periodMonths}")
        Text("Ставка: ${item.interestRate}")
        Text("Пополнение: ${item.monthlyTopUp}")
        Text("Итог: ${item.finalAmount}")
        Text("Доход: ${item.interestEarned}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            vm.delete(item, userLogin)
            onBack()
        }) {
            Text("Удалить")
        }

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}