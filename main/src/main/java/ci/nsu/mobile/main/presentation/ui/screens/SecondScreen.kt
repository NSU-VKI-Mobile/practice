package ci.nsu.mobile.main.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.navigation.Screen
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import kotlinx.coroutines.launch

@Composable
fun SecondScreenContent(
    navToScreen: (String) -> Unit,
    viewModel: DepositCalculationViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val availableRates = when {
        uiState.periodMonths.toIntOrNull() == null -> emptyList()
        uiState.periodMonths.toInt() < 6 -> listOf(15)
        uiState.periodMonths.toInt() < 12 -> listOf(10)
        else -> listOf(5)
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState)
    { data->
        Snackbar(modifier = Modifier.padding(bottom = 700.dp),
            snackbarData = data,
            shape = RoundedCornerShape(20.dp))
    }}) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Доступная процентная ставка:")
                availableRates.forEach { rate ->
                    FilterChip(
                        onClick = {
                            viewModel.updateSelectedRate(rate)
                            viewModel.interestRateUpdate(rate.toString())
                        },
                        label = { Text("${rate}%") },
                        selected = uiState.selectedInterestRate == rate,
                        leadingIcon = if (uiState.selectedInterestRate == rate) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Checkbox(checked = uiState.monthlyTopUpCheck, onCheckedChange = { viewModel.updateHasMonthlyTopUp(it) })
                Text("Ежемесячное пополнение")
            }

            if (uiState.monthlyTopUpCheck) {
                TextField(
                    value = uiState.monthlyTopUp ?: "",
                    label = { Text("Ежемесячное пополнение (₽)") },
                    onValueChange = { viewModel.monthlyTopUpUpdate(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(8.dp),
                    placeholder = {Text("1000.0")},
                    trailingIcon = {
                        if (!uiState.monthlyTopUp.isNullOrEmpty()) {
                            IconButton(onClick = {viewModel.monthlyTopUpUpdate("")}) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                            }
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navToScreen(Screen.FirstScreen.route) },
                    modifier = Modifier.padding(10.dp).width(150.dp)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        if(viewModel.validationSecondScreen(uiState.monthlyTopUpCheck)) {
                            val currentTimeMillis = System.currentTimeMillis()
                            val (finalAmount, interestEarned) = viewModel.calculateFinalAmount(
                                uiState.initialAmount.toDouble(),
                                uiState.interestRate.toInt(),
                                uiState.periodMonths.toInt(),
                                uiState.monthlyTopUp?.toDoubleOrNull()
                            )
                            viewModel.updateCalculationResult(
                                finalAmount,
                                interestEarned,
                                currentTimeMillis
                            )
                            navToScreen(Screen.ResultScreen.route)
                        }
                       else {
                            scope.launch {
                                snackbarHostState.showSnackbar(viewModel.errorMessage.value)
                            }
                        }
                    },
                    modifier = Modifier.padding(10.dp).width(150.dp)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}
