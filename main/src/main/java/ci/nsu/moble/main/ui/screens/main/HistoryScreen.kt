package ci.nsu.moble.main.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.viewmodel.HistoryViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    bottomPadding: Dp
) {
    // Koin автоматически создаст или найдет нужную ViewModel
    val viewModel: HistoryViewModel = koinViewModel()
    val history by viewModel.history.collectAsState()

    val sdf = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                actions = {
                    IconButton(onClick = { viewModel.deleteAll() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить всё",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        modifier = Modifier.padding(bottom = bottomPadding)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(history) { record ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* Детали */ }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Дата: ${sdf.format(record.calculationDate)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text("${record.interestRate}%", color = MaterialTheme.colorScheme.primary)
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Взнос: ${record.initialAmount} руб.")
                                Text(
                                    "Итог: ${"%.2f".format(record.finalAmount)} руб.",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { viewModel.delete(record) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Удалить запись",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}