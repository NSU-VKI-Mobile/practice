package ci.nsu.mobile.main.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModel
import kotlinx.coroutines.launch

@Composable
fun FirstScreenContent(navToScreen: (String) -> Unit, viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }) {
        innerPadding -> Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField(uiState.initialAmount, label = {Text("Стартовый взнос")},
            onValueChange = {
                viewModel.initialAmountUpdate(it)}, modifier = Modifier.padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        TextField(uiState.periodMonths, label = {Text("Срок вклада в месяцах")},
            onValueChange = {viewModel.periodMonthUpdate(it)}, modifier = Modifier.padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button({
                    viewModel.cleanAll()
                    navToScreen(Screen.MainScreen.route)
                   }, modifier =  Modifier.padding(10.dp)) {
                Text("<- В начало")
            }
            Button({
                when(viewModel.validationFirstScreen()){
                    true -> navToScreen(Screen.SecondScreen.route)
                    false -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(viewModel.errorMessage.value)
                        }

                    }
                }
            }, modifier = Modifier.padding(10.dp)) {
                Text("Далее ->")
                }
            }
        }
    }
}
