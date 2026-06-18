package ci.nsu.mobile.main.presentation.screens.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    viewModel: InputViewModel,
    onCalculate: (String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Шаг 2: Дополнительные параметры") }) },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Назад")
                }
                Button(
                    onClick = {
                        if (viewModel.isValidStep2()) {
                            onCalculate(
                                uiState.initialAmount,
                                uiState.periodMonths,
                                uiState.selectedInterestRate?.toString() ?: "null",
                                uiState.monthlyTopUp.ifEmpty { "null" }
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.isValidStep2()
                ) {
                    Text("Рассчитать")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Процентная ставка",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Box {
                    OutlinedTextField(
                        value = uiState.selectedInterestRate?.let { "$it%" } ?: "Нажмите, чтобы выбрать",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        enabled = uiState.availableInterestRates.isNotEmpty(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        uiState.availableInterestRates.forEach { rate ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.updateInterestRate(rate)
                                        expanded = false
                                    }
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$rate%",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.availableInterestRates.isEmpty() && uiState.periodMonths.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Для срока ${uiState.periodMonths} мес. нет доступных ставок",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            OutlinedTextField(
                value = uiState.monthlyTopUp,
                onValueChange = { viewModel.updateMonthlyTopUp(it) },
                label = { Text("Ежемесячное пополнение (необязательно)") },
                isError = uiState.monthlyTopUpError != null,
                supportingText = { uiState.monthlyTopUpError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}