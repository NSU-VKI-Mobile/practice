package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

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
            isError = uiState.showCelsiusError,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .onFocusChanged {
                    if (it.isFocused) viewModel.onCelsiusFocused()
                },
            supportingText = if (uiState.showCelsiusError) {
                { Text("Ошибка ввода Цельсий", color = Color.Red) }
            } else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = when (uiState.lastEdited) {
                EditedField.CELSIUS -> Icons.Default.ArrowDownward
                EditedField.FAHRENHEIT -> Icons.Default.ArrowUpward
                EditedField.NONE -> Icons.Default.ArrowDownward
            },
            contentDescription = "Direction",
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = viewModel::onFahrenheitChanged,
            label = { Text("Фаренгейт") },
            isError = uiState.showFahrenheitError,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .onFocusChanged {
                    if (it.isFocused) viewModel.onFahrenheitFocused()
                },
            supportingText = if (uiState.showFahrenheitError) {
                { Text("Ошибка ввода Фаренгейт", color = Color.Red) }
            } else null
        )
    }
}