package ci.nsu.mobile.main.ui.screens

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

@Composable
fun HistoryScreen(
    calculations: List<DepositCalculation>,
    isLoading: Boolean,
    error: String?,
    onItemClick: (Long) -> Unit,
    onLoad: () -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("← Назад")
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text(text = error, fontSize = 16.sp)
                }
                calculations.isEmpty() -> {
                    Text(text = "Нет сохранённых расчётов", fontSize = 18.sp)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
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
            .clickable { onClick() }  // ← ДОБАВЬ ЭТУ СТРОКУ
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