package ci.nsu.moble.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositViewModel
import ci.nsu.moble.main.Navigation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val fmt     = DecimalFormat("#,##0.00")
private val dateFmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

@Composable
fun HistoryScreen(nav: NavController, vm: DepositViewModel) {
    // collectAsState() — подписываемся на Flow, Compose перерисует экран при изменении
    val list by vm.history.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("История расчётов", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (list.isEmpty()) {
            // Если история пуста — показываем заглушку
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет сохранённых расчётов",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate(Navigation.detail(item.id)) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = dateFmt.format(Date(item.date)),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("Взнос: ${fmt.format(item.initialAmount)} ₽")
                            Text(
                                text = "Итог: ${fmt.format(item.finalAmount)} ₽",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = { nav.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}