package ci.nsu.mobile.calculations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.calculations.data.local.entities.DepositCalculation
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositHistoryScreen(
    depositViewModel: DepositViewModel,
    userId: Long,
    onCalculationClick: (DepositCalculation) -> Unit
) {
    val calculations by depositViewModel.myCalculations.collectAsState(initial = emptyList())
    val isLoading by depositViewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        depositViewModel.setUserId(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Deposits") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                calculations.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No calculations yet",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(calculations) { calculation ->
                            DepositCalculationCard(
                                calculation = calculation,
                                onClick = { onCalculationClick(calculation) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DepositCalculationCard(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    val format = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = format.format(calculation.initialAmount),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${calculation.periodMonths} months",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${calculation.interestRate}% per year",
                style = MaterialTheme.typography.bodyMedium
            )

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Final:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = format.format(calculation.finalAmount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = dateFormat.format(Date(calculation.calculationDate)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
