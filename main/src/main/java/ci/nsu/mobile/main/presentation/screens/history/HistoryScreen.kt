package ci.nsu.mobile.main.presentation.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.domain.model.DepositCalculation
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onNavigateToDetail: (Long) -> Unit
) {
    val application = LocalContext.current.applicationContext as DepositApplication
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(application.locator.depositRepository, application.locator.tokenManager)
    )

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.calculations.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("У вас пока нет сохранённых расчётов")
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.calculations) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        onClick = { onNavigateToDetail(calculation.id) }
                    )
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
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = dateFormatter.format(Date(calculation.calculationDate)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Взнос:", style = MaterialTheme.typography.bodyMedium)
                Text(text = formatter.format(calculation.initialAmount))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Итог:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = formatter.format(calculation.finalAmount),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}