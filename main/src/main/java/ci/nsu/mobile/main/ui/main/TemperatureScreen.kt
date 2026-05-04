package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TemperatureScreenContent(
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = uiState.celsius,
            onValueChange = viewModel::onCelsiusChanged,
            label = { Text("Цельсий") },
            isError = uiState.celsius.isNotBlank() && !uiState.isCelsiusValid,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = viewModel::onFahrenheitChanged,
            label = { Text("Фаренгейт") },
            isError = uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.celsius.isNotBlank() && !uiState.isCelsiusValid) {
            Text(
                text = "Ошибка ввода Цельсий",
                color = Color.Red
            )
        }

        if (uiState.fahrenheit.isNotBlank() && !uiState.isFahrenheitValid) {
            Text(
                text = "Ошибка ввода Фаренгейт",
                color = Color.Red
            )
        }
    }
}