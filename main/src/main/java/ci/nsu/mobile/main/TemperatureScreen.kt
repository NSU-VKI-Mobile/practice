package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

@Composable
fun TemperatureScreen(
    modifier: Modifier = Modifier,
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Конвертер температуры",
            style = MaterialTheme.typography.headlineSmall
        )

        // Поле Цельсия
        TextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = { Text("Градусы Цельсия (°C)") },
            isError = !uiState.isCelsiusValid,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (!uiState.isCelsiusValid) Text("Введите корректное число")
            }
        )

        // Поле Фаренгейта.
        TextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Градусы Фаренгейта (°F)") },
            isError = !uiState.isFahrenheitValid,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (!uiState.isFahrenheitValid) Text("Введите корректное число")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTemperatureScreen() {
    PracticeTheme {
        TemperatureScreen()
    }
}