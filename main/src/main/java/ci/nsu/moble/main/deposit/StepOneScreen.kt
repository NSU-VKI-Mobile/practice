package ci.nsu.moble.main.deposit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StepOneScreen(
    viewModel: DepositViewModel,
    onBackHome: () -> Unit,
    onNext: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Этап 1: Основные параметры",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = uiState.initialAmount,
            onValueChange = viewModel::updateInitialAmount,
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.months,
            onValueChange = viewModel::updateMonths,
            label = { Text("Срок вклада в месяцах") },
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onBackHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }

        Button(
            onClick = {
                if (viewModel.validateStepOne()) {
                    onNext()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Далее")
        }
    }
}