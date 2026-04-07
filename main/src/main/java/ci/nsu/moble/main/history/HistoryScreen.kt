package ci.nsu.moble.main.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onOpenDetail: (Long) -> Unit,
    onBackHome: () -> Unit
) {
    val history by viewModel.history.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            style = MaterialTheme.typography.headlineSmall
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(history) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenDetail(item.id) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Дата: ${item.dateTime}")
                        Text("Стартовый взнос: ${item.initialAmount}")
                        Text("Итоговая сумма: ${item.finalAmount}")
                    }
                }
            }
        }

        Button(
            onClick = onBackHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}