package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: Long,
    onNavigateToMain: () -> Unit
) {
    val factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(id) as T
        }
    }
    val viewModel: DetailsViewModel = viewModel(factory = factory)
    val calculation by viewModel.calculation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToMain) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (calculation == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val calc = calculation!!
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Дата расчёта: ${dateFormat.format(Date(calc.calculationDate))}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Divider()
                        Text(
                            text = "Стартовый взнос: ${String.format("%.2f", calc.initialAmount)} ₽",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Срок вклада: ${calc.periodMonths} месяцев",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Процентная ставка: ${calc.interestRate}%",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (calc.monthlyTopUp != null && calc.monthlyTopUp > 0) {
                            Text(
                                text = "Ежемесячное пополнение: ${String.format("%.2f", calc.monthlyTopUp)} ₽",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Начисленные проценты: ${String.format("%.2f", calc.interestEarned)} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Итоговая сумма: ${String.format("%.2f", calc.finalAmount)} ₽",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}