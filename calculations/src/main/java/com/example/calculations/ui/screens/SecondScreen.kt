package com.example.calculations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculations.navigation.Screens
import com.example.calculations.viewmodel.DepositCalculationViewModel
import com.example.calculations.viewmodel.DepositEvents
import com.example.ui.components.CustomButton
import com.example.ui.components.TextFieldWithOptionalStar

@Composable
fun SecondScreenContent(
    navToScreen: (String) -> Unit,
    viewModel: DepositCalculationViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val availableRates = when {
        state.periodMonths.toIntOrNull() == null -> emptyList()
        state.periodMonths.toInt() < 6 -> listOf(5)
        state.periodMonths.toInt() < 12 -> listOf(10, 5)
        else -> listOf(15, 10, 5)
    }

    LaunchedEffect(state.goToResultScreen) {
        if (state.goToResultScreen) {
            val currentTimeMillis = System.currentTimeMillis()
            viewModel.depositCalculationEvent(
                DepositEvents.CalculationFinalAmount(
                state.initialAmount.toDouble(),
                state.interestRate.toInt(),
                state.periodMonths.toInt(),
                state.monthlyTopUp?.toDoubleOrNull(),
                currentTimeMillis
            ))
            navToScreen(Screens.ResultScreen.route)
        }
    }
    Scaffold() { innerPadding ->
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
                            viewModel.depositCalculationEvent(DepositEvents.SelectedRateUpdate(rate))
                            viewModel.depositCalculationEvent(DepositEvents.InterestRateChanged(rate.toString()))
                        },
                        label = { Text("${rate}%") },
                        selected = state.selectedInterestRate == rate,
                        leadingIcon = if (state.selectedInterestRate == rate) {
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
                Checkbox(checked = state.monthlyTopUpCheck,
                    onCheckedChange = {
                        viewModel.depositCalculationEvent(DepositEvents.IsMonthlyTopUpCheck(it))
                    })
                Text("Ежемесячное пополнение")
            }
            if ("interestRate" in state.errorFieldsSecondScreen) {
                Text("Выберете процентную ставку", color = Color.Red,
                    modifier = Modifier.padding(bottom = 10.dp))
            }
            if (state.monthlyTopUpCheck) {
                TextFieldWithOptionalStar(
                    value = state.monthlyTopUp ?: "",
                    placeholder = "Ежемесячное пополнение (₽)",
                    onValueChange = {
                        viewModel.depositCalculationEvent(DepositEvents.MonthlyTopUpChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(8.dp),
                    trailingIcon = {
                        if (!state.monthlyTopUp.isNullOrEmpty()) {
                            IconButton(onClick = {
                                viewModel.depositCalculationEvent(DepositEvents.MonthlyTopUpChanged(""))
                            }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                            }
                        }
                    },
                    isError = "monthlyTopUp" in state.errorFieldsSecondScreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    onClick = {
                        navToScreen(Screens.FirstScreen.route)
                        viewModel.depositCalculationEvent(DepositEvents.GoToSecondScreen(false))
                    },
                    modifier = Modifier.width(150.dp),
                    title = "Назад"
                )
                CustomButton(
                    onClick = {
                        viewModel.depositCalculationEvent(DepositEvents.ValidationSecondScreen(state.monthlyTopUpCheck))
                    },
                    modifier = Modifier.padding(10.dp).width(150.dp),
                    title = "Рассчитать"
                )
            }
        }
    }
}
