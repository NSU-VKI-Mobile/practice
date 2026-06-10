// Task_5: Детальная информация о расчёте.

package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Screen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreen(viewModel: MainViewModel) {
    val calc by viewModel.selectedCalculation.collectAsStateWithLifecycle()
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val dateFmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Детали расчёта", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        val c = calc
        if (c != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    DetailRow("Дата", dateFmt.format(Date(c.calculationDate)))
                    DetailRow("Стартовый взнос", fmt.format(c.initialAmount) + " ₽")
                    DetailRow("Срок вклада", "${c.periodMonths} мес.")
                    DetailRow("Процентная ставка", "${c.interestRate}%")
                    if (c.monthlyTopUp != null) {
                        DetailRow("Ежемесячное пополнение", fmt.format(c.monthlyTopUp) + " ₽")
                    }
                    DetailRow("Итоговая сумма", fmt.format(c.finalAmount) + " ₽", bold = true)
                    DetailRow("Начисленные проценты", fmt.format(c.interestEarned) + " ₽", bold = true)
                }
            }
        } else {
            Text("Загрузка...", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.navigateTo(Screen.History) }) {
            Text("Назад к истории")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, bold: Boolean = false) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp)
        Text(value, fontSize = 16.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}
