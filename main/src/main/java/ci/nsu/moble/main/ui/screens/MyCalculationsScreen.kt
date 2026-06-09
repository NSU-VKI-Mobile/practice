package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.data.repository.DepositRepository
import ci.nsu.moble.main.data.models.DepositCalculation
import ci.nsu.moble.main.viewmodel.DepositUiState
import ci.nsu.moble.main.viewmodel.DepositViewModel
import ci.nsu.moble.main.viewmodel.DepositViewModelFactory
import java.text.DecimalFormat

@Composable
fun MyCalculationsScreen(
    userId: Long,
    repository: DepositRepository,
    onItemClick: (DepositCalculation) -> Unit
) {
    val viewModel: DepositViewModel = viewModel(
        factory = DepositViewModelFactory(repository, userId)
    )

    val state by viewModel.state.collectAsState()
    val decimalFormat = DecimalFormat("#,##0.00")

    when (state) {
        is DepositUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DepositUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${(state as DepositUiState.Error).message}")
            }
        }
        is DepositUiState.Success -> {
            val calculations = (state as DepositUiState.Success).calculations
            if (calculations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет сохранённых расчётов")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(calculations) { calculation ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(calculation) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = calculation.getFormattedDate(),
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = "💰 ${decimalFormat.format(calculation.initialAmount)} руб → ${decimalFormat.format(calculation.finalAmount)} руб",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "📅 ${calculation.periodMonths} мес, ${calculation.interestRate}%",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}