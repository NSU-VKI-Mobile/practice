package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stage2Screen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.depositState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val rateOptions = when {
        state.periodMonths.toIntOrNull() == null -> emptyList()
        state.periodMonths.toInt() < 6 -> listOf(15.0)
        state.periodMonths.toInt() < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Дополнительные параметры")

        Spacer(modifier = Modifier.height(32.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = state.interestRate?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                rateOptions.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text("$rate%") },
                        onClick = {
                            viewModel.setInterestRate(rate)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.monthlyTopUp,
            onValueChange = { viewModel.updateMonthlyTopUp(it) },
            label = { Text("Ежемесячное пополнение (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.errorMessage!!,
                color = androidx.compose.ui.graphics.Color.Red
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.validateStage2()) {
                    viewModel.calculateResult()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
    }
}