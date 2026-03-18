package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.DepositViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ResultScreen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.depositState.collectAsState()
    val format = NumberFormat.getNumberInstance(Locale("ru", "RU"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Результат расчёта",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Стартовый взнос: ${format.format(state.initialAmount.toDoubleOrNull() ?: 0.0)} руб.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Срок вклада: ${state.periodMonths} мес.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Процентная ставка: ${state.interestRate ?: 0.0}%")
                Spacer(modifier = Modifier.height(8.dp))
                if (state.monthlyTopUp.isNotBlank()) {
                    Text("Ежемесячное пополнение: ${format.format(state.monthlyTopUp.toDoubleOrNull() ?: 0.0)} руб.")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text("Итоговая сумма: ${format.format(state.finalAmount)} руб.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Начисленные проценты: ${format.format(state.interestEarned)} руб.")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveCalculation()
                viewModel.navigateToMain()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.navigateToMain() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}