package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import ci.nsu.mobile.main.viewmodel.InputViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOneScreen(
    onNavigateToNext: (Double, Int) -> Unit,
    onNavigateToMain: () -> Unit
) {
    val viewModel: InputViewModel = viewModel()

    val initialAmount by viewModel.initialAmount.collectAsState()
    val periodMonths by viewModel.periodMonths.collectAsState()
    val initialAmountError by viewModel.initialAmountError.collectAsState()
    val periodError by viewModel.periodError.collectAsState()
    val isValid by viewModel.isValid.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Параметры вклада") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            OutlinedTextField(
                value = if (initialAmount > 0) initialAmount.toString() else "",
                onValueChange = { viewModel.updateInitialAmount(it) },
                label = { Text("Стартовый взнос") },
                isError = initialAmountError != null,
                supportingText = { if (initialAmountError != null) Text(initialAmountError!!) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = if (periodMonths > 0) periodMonths.toString() else "",
                onValueChange = { viewModel.updatePeriodMonths(it) },
                label = { Text("Срок вклада (месяцы)") },
                isError = periodError != null,
                supportingText = { if (periodError != null) Text(periodError!!) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNavigateToMain,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = { if (isValid) onNavigateToNext(initialAmount, periodMonths) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Далее")
                }
            }
        }
    }
}