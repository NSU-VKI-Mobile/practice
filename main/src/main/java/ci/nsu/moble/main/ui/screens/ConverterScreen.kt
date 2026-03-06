package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.vm.TemperatureViewModel

@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        TextField(
            value = uiState.celsius,
            onValueChange = { newText ->
                viewModel.onCelsiusChanged(newText)
            }
        )
        TextField(
            value = uiState.fahrenheit,
            onValueChange = { newText ->
                viewModel.onFahrenheitChanged(newText)
            }
        )
    }
}

