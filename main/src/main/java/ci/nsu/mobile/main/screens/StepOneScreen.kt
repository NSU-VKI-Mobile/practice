// ui/screens/StepOneScreen.kt
package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodels.StepOneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOneScreen(
    viewModel: StepOneViewModel = viewModel(),
    onBack: () -> Unit,
    onNext: (Double, Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шаг 1: Основные параметры") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.initialAmount,
                onValueChange = viewModel::updateInitialAmount,
                label = { Text("Стартовый взнос *") },
                isError = state.initialAmountError != null,
                supportingText = state.initialAmountError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.periodMonths,
                onValueChange = viewModel::updatePeriodMonths,
                label = { Text("Срок вклада (месяцев) *") },
                isError = state.periodMonthsError != null,
                supportingText = state.periodMonthsError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = {
                        if (viewModel.isValid()) {
                            onNext(viewModel.getInitialAmount(), viewModel.getPeriodMonths())
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.isValid()
                ) {
                    Text("Далее")
                }
            }
        }
    }
}