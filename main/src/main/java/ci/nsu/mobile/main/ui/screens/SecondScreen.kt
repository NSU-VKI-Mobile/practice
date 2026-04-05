package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import kotlinx.coroutines.launch


//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SecondScreenContent(navScreens: NavController,
                        viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val checkState = remember { mutableStateOf(false) }
    var selectedRate by remember { mutableStateOf(0) }
    val interestRates = listOf(15, 10, 5)
    var errorMessage = ""
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Доступная процентная ставка:")
                interestRates.forEach { rate ->
                    FilterChip(
                        onClick = {
                            selectedRate = rate
                            viewModel.interestRateUpdate(rate.toString())
                        },
                        label = { Text("${rate.toInt()}%") },
                        selected = selectedRate == rate,
                        leadingIcon = if (selectedRate == rate) {
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
            Row(Modifier.fillMaxWidth()) {
                Checkbox(checked = checkState.value, onCheckedChange = { checkState.value = it })
                Text("Ежемесячное пополнение")
            }
            if (checkState.value) {
                TextField(
                    uiState.monthlyTopUp.toString(), label = { Text("Ежемесячное пополнение") },
                    onValueChange = { viewModel.monthlyTopUpUpdate(it) })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    { navScreens.navigate(Routes.FistScreen.route) },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Назад")
                }
                Button(
                    {
                        errorMessage = viewModel.validationSecondScreen(checkState.value)
                        val currentTimeMillis = System.currentTimeMillis()
                        if (errorMessage == "") {
                            viewModel.calculateFinalAmount(uiState.initialAmount.toDouble(),
                                uiState.interestRate.toInt(),
                                uiState.periodMonths.toInt(),
                                uiState.monthlyTopUp?.toDoubleOrNull())
                            viewModel.calculateDate(currentTimeMillis)
                            navScreens.navigate(Routes.ResultScreen.route)
                        }
                        else {
                            scope.launch {
                                snackbarHostState.showSnackbar(errorMessage)
                            }
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}
