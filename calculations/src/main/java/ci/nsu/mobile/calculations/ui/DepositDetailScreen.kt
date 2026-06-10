package ci.nsu.mobile.calculations.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack  //
import androidx.compose.material.icons.filled.Delete

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.calculations.data.local.entities.DepositCalculation
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import java.util.Date


import androidx.compose.material3.Icon




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositDetailScreen(
    depositViewModel: DepositViewModel,
    calculationId: Long,
    userId: Long,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val format = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))





    LaunchedEffect(calculationId, userId) {
        depositViewModel.setUserId(userId)
        scope.launch {
            isLoading = true
            try {
                calculation = depositViewModel.getCalculationById(calculationId)
            } catch (e: Exception) {
                calculation = null
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculation Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        enabled = calculation != null
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
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
                calculation == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Calculation not found",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBack) {
                            Text("Go Back")
                        }
                    }
                }
                else -> {
                    calculation?.let { calc ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Input Parameters",
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Divider()
                                    DetailRow("Initial Amount:", format.format(calc.initialAmount))
                                    DetailRow("Period:", "${calc.periodMonths} months")
                                    DetailRow("Interest Rate:", "${calc.interestRate}%")
                                }
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Results",
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Divider()
                                    DetailRow(
                                        "Final Amount:",
                                        format.format(calc.finalAmount),
                                        isBold = true
                                    )
                                    DetailRow(
                                        "Interest Earned:",
                                        format.format(calc.interestEarned),
                                        isBold = true
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Additional Information",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    DetailRow(
                                        "Calculation Date:",
                                        dateFormat.format(Date(calc.calculationDate))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Calculation") },
            text = { Text("Are you sure you want to delete this calculation?") },
            confirmButton = {
                Button(
                    onClick = {
                        calculation?.let {
                            depositViewModel.deleteCalculation(it)
                            showDeleteDialog = false
                            onBack()
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            color = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}