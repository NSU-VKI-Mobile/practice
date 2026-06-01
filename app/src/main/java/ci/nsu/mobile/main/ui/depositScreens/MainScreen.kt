package ci.nsu.mobile.main.ui.depositScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.ui.components.CustomButton
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.deposit.DepositEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    navToScreen: (String) -> Unit,
    viewModel: DepositCalculationViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold() { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomButton(
                {
                    viewModel.depositCalculationEvent(DepositEvents.CleanAll)
                    navToScreen(Screens.FirstScreen.route)
                },
                "Новый расчет",
                modifier = Modifier.width(200.dp)
            )
            if (!state.initialAmount.isEmpty()) {
                CustomButton(
                    {
                        navToScreen(Screens.FirstScreen.route)
                    },
                    "Продолжить расчет",
                    modifier = Modifier.width(200.dp)

                )
            }
        }
    }
}
