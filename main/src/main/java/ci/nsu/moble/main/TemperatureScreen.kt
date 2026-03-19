package ci.nsu.moble.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Конвертер температуры",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = { Text("Цельсий")},
            isError = uiState.celsius.isNotEmpty() && !uiState.isCelsiusValid,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Фаренгейт")},
            isError = uiState.fahrenheit.isNotEmpty() && !uiState.isFahrenheitValid,
            modifier = Modifier.fillMaxWidth()
        )
    }
}