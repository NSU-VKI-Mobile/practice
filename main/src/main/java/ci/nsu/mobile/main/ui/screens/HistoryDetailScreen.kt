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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDetailScreen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val calculation by viewModel.selectedCalculation.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Детали расчёта",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (calculation != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Дата: ${dateFormat.format(Date(calculation!!.calculationDate))}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Стартовый взнос: ${calculation!!.initialAmount} руб.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Срок вклада: ${calculation!!.periodMonths} мес.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Процентная ставка: ${calculation!!.interestRate}%")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (calculation!!.monthlyTopUp != null) {
                        Text("Ежемесячное пополнение: ${calculation!!.monthlyTopUp} руб.")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text("Итоговая сумма: ${calculation!!.finalAmount} руб.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Начисленные проценты: ${calculation!!.interestEarned} руб.")
                }
            }
        } else {
            Text("Расчёт не найден")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}