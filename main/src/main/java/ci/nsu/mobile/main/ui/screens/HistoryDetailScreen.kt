package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    id: Int,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val depositState = viewModel.getDepositById(id).collectAsState(initial = null)
    val deposit = depositState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта", color = Color(0xFF1565C0)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (deposit == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Загрузка...", color = Color.Gray)
            }
        } else {
            val d = deposit!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Дата: ${d.dateTime}",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        RowItem("Стартовый взнос:", "${fmt(d.startAmount)} руб.")
                        RowItem("Срок вклада:", "${d.months} мес.")
                        RowItem("Процентная ставка:", "${d.rate}%")
                        RowItem(
                            "Ежемесячное пополнение:",
                            if (d.monthlyAddition > 0)
                                "${fmt(d.monthlyAddition)} руб." else "нет"
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.LightGray
                        )

                        Text(
                            text = "Итоговая сумма: ${fmt(d.totalAmount)} руб.",
                            fontSize = 18.sp,
                            color = Color(0xFF1565C0)
                        )
                        Text(
                            text = "Начислено процентов: ${fmt(d.earnedInterest)} руб.",
                            fontSize = 15.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontSize = 14.sp)
    }
}

private fun fmt(v: Double) = String.format("%.2f", v)