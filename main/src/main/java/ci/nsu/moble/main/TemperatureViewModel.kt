package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsiusValue = newValue.toDoubleOrNull()

            val fahrenheit = if (celsiusValue != null) {
                String.format("%.2f", celsiusValue * 9 / 5 + 32)
            } else {
                ""
            }

            currentState.copy(
                celsius = newValue,
                fahrenheit = fahrenheit
            )
        }
    }

    fun onFahrenheitChanged(newValue: String) {
        _uiState.update { currentState ->
            val fahrenheitValue = newValue.toDoubleOrNull()

            val celsius = if (fahrenheitValue != null) {
                String.format("%.2f", (fahrenheitValue - 32) * 5 / 9)
            } else {
                ""
            }

            currentState.copy(
                fahrenheit = newValue,
                celsius = celsius
            )
        }
    }
}