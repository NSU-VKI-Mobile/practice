package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.db.DepositCalculation
import ci.nsu.mobile.main.di.LocalRepository
import ci.nsu.mobile.main.ui.viewmodel.HistoryViewModel
import ci.nsu.mobile.main.util.formatCurrency
import ci.nsu.mobile.main.util.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onItemClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val repository = LocalRepository.current
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(repository)
    )
    val calculations by viewModel.calculations.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        if (calculations.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                Text("История пуста", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations, key = { it.id }) { calc ->
                    CalculationItem(calculation = calc) {
                        onItemClick(calc.id)
                    }
                }
            }
        }
    }
}

@Composable
fun CalculationItem(calculation: DepositCalculation, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = calculation.calculationDate.formatDate())
            Text(text = "Взнос: ${calculation.initialAmount.formatCurrency()}")
            Text(text = "Итог: ${calculation.finalAmount.formatCurrency()}")
        }
    }
}