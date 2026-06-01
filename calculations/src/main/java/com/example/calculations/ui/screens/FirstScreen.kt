package com.example.calculations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculations.navigation.Screens
import com.example.calculations.viewmodel.DepositCalculationViewModel
import com.example.calculations.viewmodel.DepositEvents
import com.example.ui.components.CustomButton
import com.example.ui.components.TextFieldWithOptionalStar

@Composable
fun FirstScreenContent(navToScreen: (String) -> Unit, viewModel: DepositCalculationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.goToSecondScreen) {
        if (state.goToSecondScreen)
            navToScreen(Screens.SecondScreen.route)
    }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextFieldWithOptionalStar(
                value = state.initialAmount,
                placeholder = "Стартовый взнос (₽)",
                onValueChange = {
                    viewModel.depositCalculationEvent(DepositEvents.InitialAmountChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (!state.initialAmount.isEmpty()) {
                        IconButton(onClick = {
                            viewModel.depositCalculationEvent(DepositEvents.InitialAmountChanged(""))
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                },
                isError = "initialAmount" in state.errorFieldsFirstScreen,
                supportingText = "Проверьте поле"
            )
            TextFieldWithOptionalStar(
                value = state.periodMonths,
                placeholder = "Срок вклада в месяцах",
                onValueChange = {
                    viewModel.depositCalculationEvent(DepositEvents.PeriodMonthsChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (!state.periodMonths.isEmpty()) {
                        IconButton(onClick = {
                            viewModel.depositCalculationEvent(DepositEvents.PeriodMonthsChanged(""))
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                },
                isError = "periodMonths" in state.errorFieldsFirstScreen,
                supportingText = "Проверьте поле"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically

            ) {
                CustomButton(
                    onClick = {
                        viewModel.depositCalculationEvent(DepositEvents.CleanAll)
                        navToScreen(Screens.MainScreen.route)
                    },
                    title = "<- В начало",
                    modifier = Modifier.width(150.dp)
                )
                CustomButton(
                    onClick = {
                        viewModel.depositCalculationEvent(DepositEvents.ValidationFirstScreen)
                    },
                    title = "Далее ->",
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
}
