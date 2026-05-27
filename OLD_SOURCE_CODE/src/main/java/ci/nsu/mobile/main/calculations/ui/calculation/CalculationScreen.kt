package ci.nsu.mobile.main.calculations.ui.calculation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CalculationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAdditional: (amount: String, period: String) -> Unit
) {
    val viewModel: CalculationViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчет вклада") },
                navigationIcon = {
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Назад")
                    }
                },
            )
        }
    ) {
            paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Поле для суммы вклада
            DepositAmountInput(
                value = uiState.depositAmount,
                onValueChange = viewModel::updateDepositAmount,
                isValid = uiState.isAmountValid
            )

            // Поле для срока вклада
            DepositPeriodInput(
                value = uiState.depositTerm,
                onValueChange = viewModel::updateDepositTerm,
                isValid = uiState.isTermValid
            )

            Button(
                onClick = {
                    onNavigateToAdditional(
                        uiState.depositAmount,
                        uiState.depositTerm
                    )
                },
                enabled = uiState.canProceed,
            ) {
                Text(
                    text = "Далее",
                )
            }
        }
    }
}

@Composable
fun DepositAmountInput(
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Сумма вклада") },
        placeholder = { Text("Например: 100000") },
        isError = !isValid && value.isNotBlank(),
        supportingText = {
            if (!isValid && value.isNotBlank()) {
                Text(
                    text = "Введите корректное число"
                )
            }
        },
        singleLine = true
    )
}

@Composable
fun DepositPeriodInput(
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Срок вклада (месяцев)") },
        placeholder = { Text("Например: 12") },
        isError = !isValid && value.isNotBlank(),
        supportingText = {
            if (!isValid && value.isNotBlank()) {
                Text(
                    text = "Введите корректное число",
                )
            }
        },
        singleLine = true
    )
}