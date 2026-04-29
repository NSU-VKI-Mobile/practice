package ci.nsu.mobile.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TemperatureScreen(
    modifier: Modifier = Modifier,
    viewModel: TemperatureViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TemperatureContent(
        modifier = modifier,
        uiState = uiState,
        onCelsius = viewModel::onCelsiusChanged,
        onFahrengate = viewModel::onFahrenheitChanged
    )
}

@Composable
fun TemperatureContent(
    modifier: Modifier = Modifier,
    uiState: TemperatureUiState,
    onCelsius: () -> Unit,
    onFahrengate: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Конвертер Температуры", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = )
    }
}