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

            val cDegree = nextState.celsius.toDoubleOrNull()

            if (cDegree != null) {
                val fResult = cDegree * 9 / 5 + 32
                nextState.copy(fahrenheit = "%.2f".format(fResult))
            } else {
                nextState.copy(fahrenheit = "")
            }
        }
    }

    fun onFahrenheitChanged(newValue: String) {
        _uiState.update { currentState ->
            val nextState = currentState.copy(fahrenheit = newValue)

            val fDegree = nextState.fahrenheit.toDoubleOrNull()

            if (fDegree != null) {
                val cResult = (fDegree - 32) * 5 / 9
                nextState.copy(celsius = "%.2f".format(cResult))
            } else {
                nextState.copy(celsius = "")
            }
        }
    }

    fun toggleDirection() {
        _uiState.update { it.copy(isCelsiusEntry = !it.isCelsiusEntry) }
    }
}