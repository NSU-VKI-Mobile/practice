package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newCelsius: String) {
        _uiState.update { currentState ->
            val nextState = currentState.copy(celsius = newCelsius)
            nextState.copy(fahrenheit = if (nextState.isCelsiusValid) nextState.convertedToFahrenheit else "")
        }
    }

    fun onFahrenheitChanged(newFahrenheit: String) {
        _uiState.update { currentState ->
            val nextState = currentState.copy(fahrenheit = newFahrenheit)
            nextState.copy(celsius = if (nextState.isFahrenheitValid) nextState.convertedToCelsius else "")
        }
    }
}