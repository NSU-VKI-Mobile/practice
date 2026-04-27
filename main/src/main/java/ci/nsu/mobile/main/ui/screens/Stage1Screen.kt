package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.Screen

@Composable
fun Stage1Screen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.depositState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ввод основных параметров")

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = { viewModel.updateInitialAmount(it) },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.periodMonths,
            onValueChange = { viewModel.updatePeriodMonths(it) },
            label = { Text("Срок вклада (месяцев)") },
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
            onClick = { viewModel.navigateToMain() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.validateStage1()) {
                    viewModel.navigateTo(Screen.Stage2)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Далее")
        }
    }
}