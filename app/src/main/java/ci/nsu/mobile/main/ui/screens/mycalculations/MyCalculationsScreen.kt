package ci.nsu.mobile.main.ui.screens.mycalculations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.database.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme

@Composable
fun MyCalculationsScreen(
    calculations: List<DepositCalculation>,
    isLoading: Boolean,
    error: String?,
    onItemClick: (Long) -> Unit,
    onRefresh: () -> Unit
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                    Button(onClick = onRefresh) {
                        Text("Повторить")
                    }
                }
            }
            calculations.isEmpty() -> Text("Нет сохранённых расчётов")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(calculations) { calculation ->
                        HistoryItem(
                            calculation = calculation,
                            onClick = { onItemClick(calculation.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(calculation.calculationDate))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = dateStr, fontSize = 14.sp)
            Text(text = "Стартовый взнос: ${calculation.initialAmount} руб.", fontSize = 16.sp)
            Text(text = "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} руб.", fontSize = 16.sp)
        }
    }
}