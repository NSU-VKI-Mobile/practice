// Task_5: История расчётов — список сохранённых расчётов.

package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.data.DepositCalculation
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Screen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    val items by viewModel.calculations.collectAsStateWithLifecycle()
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val dateFmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("История расчётов", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.navigateTo(Screen.Main) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items, key = { it.id }) { calc ->
                HistoryItem(calc, fmt, dateFmt,
                    onItemClick = {
                        viewModel.loadCalculationDetail(calc.id)
                        viewModel.navigateTo(Screen.Detail(calc.id))
                    },
                    onDeleteClick = {
                        viewModel.deleteCalculation(calc.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(
    calc: DepositCalculation,
    fmt: NumberFormat,
    dateFmt: SimpleDateFormat,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dateFmt.format(Date(calc.calculationDate)),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Вклад: ${fmt.format(calc.initialAmount)} ₽ → ${fmt.format(calc.finalAmount)} ₽",
                    fontSize = 16.sp
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(40.dp)
            ) {
                // Используйте стандартную иконку удаления или создайте свою
                // Для этого примера предполагается, что у вас есть иконка ic_delete
                // Если нет, можно использовать Text("🗑️") как временное решение
                Text("🗑️", fontSize = 24.sp)
            }
        }
    }
}