package ci.nsu.mobile.main.presentation.screens.input

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    viewModel: InputViewModel,
    onNavigateToHome: () -> Unit,
    onNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Шаг 1: Основные параметры") })
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onNavigateToHome, modifier = Modifier.weight(1f)) {
                    Text("В начало")
                }
                Button(
                    onClick = { if (viewModel.isValidStep1()) onNext() },
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.isValidStep1()
                ) {
                    Text("Далее")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            OutlinedTextField(
                value = uiState.initialAmount,
                onValueChange = { viewModel.updateInitialAmount(it) },
                label = { Text("Стартовый взнос") },
                isError = uiState.initialAmountError != null,
                supportingText = { uiState.initialAmountError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.periodMonths,
                onValueChange = { viewModel.updatePeriodMonths(it) },
                label = { Text("Срок вклада (месяцев)") },
                isError = uiState.periodMonthsError != null,
                supportingText = { uiState.periodMonthsError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}