// Task_5: Экран результата — карточка с показателями + кнопки.

package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import java.util.Locale

@Composable
fun ResultScreen(viewModel: MainViewModel) {
    val state by viewModel.result.collectAsStateWithLifecycle()
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Результат расчёта", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ResultRow("Стартовый взнос", fmt.format(state.initialAmount) + " ₽")
                ResultRow("Срок вклада", "${state.periodMonths} мес.")
                ResultRow("Процентная ставка", "${state.interestRate}%")
                val topUp = state.monthlyTopUp
                if (topUp != null) {
                    ResultRow("Ежемесячное пополнение", fmt.format(topUp) + " ₽")
                }
                ResultRow("Итоговая сумма", fmt.format(state.finalAmount) + " ₽",
                    bold = true)
                ResultRow("Начисленные проценты", fmt.format(state.interestEarned) + " ₽",
                    bold = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.saved) {
            Text("✓ Сохранено", color = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = viewModel::saveResult,
                modifier = Modifier.weight(1f),
                enabled = !state.saved
            ) {
                Text("Сохранить")
            }
            Button(
                onClick = { viewModel.navigateTo(Screen.Main) },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp)
        Text(value, fontSize = 16.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }

}
