package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(vm: DepositViewModel) {

    val list by vm.history.collectAsState(initial = emptyList())

    if (list.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("История пуста")
        }
    } else {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())) {
            Button(
                onClick = { vm.deleteAll() }
            ) {
                Text("Удалить все")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {

                items(list) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Старт: ${String.format("%.2f", item.initialAmount)}"
                            )

                            Text(
                                "Итог: ${String.format("%.2f", item.finalAmount)}"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { vm.delete(item) }
                            ) {
                                Text("Удалить")
                            }
                        }
                    }
                }
            }
        }


    }
}