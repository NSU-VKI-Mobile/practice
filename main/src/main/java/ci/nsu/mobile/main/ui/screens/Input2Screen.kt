package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.vm.DepositsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input2Screen(
    onBackClick: () -> Unit,
    onCalcClick: () -> Unit,
    viewModel: DepositsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    Column() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = uiState.interestRate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Введите процент вклада") } ,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                isError = !uiState.isInterestRateValid
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                uiState.availableInterestRate.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text(rate.toString()) },
                        onClick = {
                            viewModel.setInterestRate(rate.toString())
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = uiState.monthlyTopUp,
            onValueChange = { newText -> viewModel.setMonthlyTopUp(newText) },
            label = { Text("Введите сумму ежемесячного пополнения (необязательное)") },
            isError = !uiState.isMonthlyTopUpValid
        )
        Button(onClick = onCalcClick) {
            Text("Рассчитать")
        }
        Button(onClick = onBackClick) {
            Text("Назад")
        }

    }
}

