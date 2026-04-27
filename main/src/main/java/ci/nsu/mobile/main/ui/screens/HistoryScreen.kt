package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.Data.Local.DepositEntity
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HistoryScreen(navController: NavController, viewModel: DepositViewModel) {
    // Превращаем Flow из ViewModel в состояние, которое понимает Compose
    val historyItems by viewModel.history.collectAsState(initial = emptyList())

    // Дальше используем historyItems в LazyColumn
    LazyColumn {
        items(historyItems) { item ->
            HistoryItemCard(item) {
                viewModel.loadFromHistory(item)
                navController.navigate("result")
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: DepositEntity, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(item.calculationDate))

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = dateString, style = MaterialTheme.typography.labelMedium)
                Text(text = "Итог: ${"%.2f".format(item.finalAmount)}", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Взнос: ${item.initialAmount}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}