package ci.nsu.mobile.calculations.ui.screens.mycalculations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.calculations.data.DepositCalculation
import java.text.SimpleDateFormat
import java.util.*

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                    Button(onClick = onRefresh) { Text("Повторить") }
                }
            }
            calculations.isEmpty() -> Text("Нет сохранённых расчётов", fontSize = 18.sp)
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(calculations) { calculation ->
                        MyCalculationItem(
                            calculation = calculation,
                            onClick = { onItemClick(calculation.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyCalculationItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    val dateStr = remember(calculation.calculationDate) {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(calculation.calculationDate))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Text(text = dateStr, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = "Стартовый взнос: ${calculation.initialAmount} руб.", fontSize = 16.sp)
            Text(text = "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} руб.", fontSize = 16.sp)
        }
    }
}