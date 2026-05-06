package ci.nsu.mobile.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding()

    ) {
        TextField(
            value = uiState.celsius,
            onValueChange = viewModel::onCelsiusChanged,
            label = {Text("Цельсия")},
            isError = !uiState.isCelsiusValid && uiState.celsius.isNotBlank(),
            supportingText = {
                if (!uiState.isCelsiusValid && uiState.celsius.isNotBlank()){
                    Text("Введите число")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = uiState.fahrenheit,
            onValueChange = viewModel::onFahrenheitChanged,
            label = {Text("Фаренгейт")},
            isError = !uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank(),
            supportingText = {
                if (!uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank()){
                    Text("Введите число")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}