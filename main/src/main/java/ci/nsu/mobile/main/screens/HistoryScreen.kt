package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(vm: DepositViewModel) {

    val list by vm.history.collectAsState(initial = emptyList())

    // Формат даты
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    if (list.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("История пуста")
        }

    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {

            items(list) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = formatter.format(Date(item.date)),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Стартовый взнос: ${
                                String.format("%.2f", item.initialAmount)
                            }",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Итоговая сумма: ${
                                String.format("%.2f", item.finalAmount)
                            }",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}