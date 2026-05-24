package ci.nsu.mobile.main.ui.depositScreens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.deposit.DepositEvents

@Composable
fun FirstScreenContent(navToScreen: (String) -> Unit, viewModel: DepositCalculationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.goToSecondScreen) {
        if (state.goToSecondScreen) {
            navToScreen(Screens.SecondScreen.route)
        }
    }
    Scaffold() {innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
        TextField(state.initialAmount, label = {Text("Стартовый взнос (₽)")},
            onValueChange = {
                viewModel.depositCalculationEvent(DepositEvents.InitialAmountChanged(it))
            }, modifier = Modifier.padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {Text("1000.0")},
            trailingIcon = {
                if (!state.initialAmount.isEmpty()) {
                    IconButton(onClick = {
                        viewModel.depositCalculationEvent(DepositEvents.InitialAmountChanged(""))
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            })
        TextField(state.periodMonths, label = {Text("Срок вклада в месяцах")},
            onValueChange = {
                viewModel.depositCalculationEvent(DepositEvents.PeriodMonthsChanged(it))
            }, modifier = Modifier.padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {Text("6")},
            trailingIcon = {
                if (!state.periodMonths.isEmpty()) {
                    IconButton(onClick = {
                        viewModel.depositCalculationEvent(DepositEvents.PeriodMonthsChanged(""))
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            }
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button({
                    viewModel.depositCalculationEvent(DepositEvents.CleanAll)
                    navToScreen(Screens.MainScreen.route)
                    }, modifier =  Modifier.padding(10.dp).width(150.dp)) {
                Text("<- В начало")
            }
            Button({
                viewModel.depositCalculationEvent(DepositEvents.ValidationFirstScreen)
            }, modifier = Modifier.padding(10.dp).width(150.dp)) {
                Text("Далее ->")
                }
            }
        }
    }
}
