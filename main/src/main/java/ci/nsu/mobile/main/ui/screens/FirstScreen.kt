package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import kotlinx.coroutines.launch


@Composable
fun FirstScreenContent(navScreen: NavController, viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var errorMessage = ""
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }) {
        innerPadding -> Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField(uiState.initialAmount, label = {Text("Стартовый взнос")},
            onValueChange = {
                viewModel.initialAmountUpdate(it)}, modifier = Modifier.padding(10.dp))
        TextField(uiState.periodMonths, label = {Text("Срок вклада в месяцах")},
            onValueChange = {viewModel.periodMonthUpdate(it)}, modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button({
                    viewModel.cleanAll()
                    navScreen.navigate(Routes.MainScreen.route)
                   }, modifier =  Modifier.padding(10.dp)) {
                Text("<- В начало")
            }
            Button({
                errorMessage = viewModel.validationFirstScreen()
                if (errorMessage == "") {
                    navScreen.navigate(Routes.SecondScreen.route)
                }
                else {
                    scope.launch {
                        snackbarHostState.showSnackbar(errorMessage)
                    }
                }
            }, modifier = Modifier.padding(10.dp)) {
                Text("Далее ->")
                }
            }
        }
    }
}
