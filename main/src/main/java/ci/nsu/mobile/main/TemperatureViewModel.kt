package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = "",
    val isCelsiusValid: Boolean = true,
    val isFahrenheitValid: Boolean = true
)

class TemperatureViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(celsiusInput: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedState = currentState.copy(
                    celsius = celsiusInput,
                    isCelsiusValid = true
                )

                if (celsiusInput.isNotEmpty()) {
                    val celsiusValue = celsiusInput.toDoubleOrNull()
                    if (celsiusValue != null) {
                        val fahrenheitValue = (celsiusValue * 9/5) + 32
                        updatedState.copy(
                            fahrenheit = String.format("%.2f", fahrenheitValue),
                            isFahrenheitValid = true
                        )
                    } else {
                        updatedState.copy(
                            fahrenheit = "",
                            isCelsiusValid = false
                        )
                    }
                } else {
                    updatedState.copy(
                        fahrenheit = "",
                        isFahrenheitValid = true
                    )
                }
            }
        }
    }

    fun onFahrenheitChanged(fahrenheitInput: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedState = currentState.copy(
                    fahrenheit = fahrenheitInput,
                    isFahrenheitValid = true
                )

                if (fahrenheitInput.isNotEmpty()) {
                    val fahrenheitValue = fahrenheitInput.toDoubleOrNull()
                    if (fahrenheitValue != null) {
                        val celsiusValue = (fahrenheitValue - 32) * 5/9
                        updatedState.copy(
                            celsius = String.format("%.2f", celsiusValue),
                            isCelsiusValid = true
                        )
                    } else {
                        updatedState.copy(
                            celsius = "",
                            isFahrenheitValid = false
                        )
                    }
                } else {
                    updatedState.copy(
                        celsius = "",
                        isCelsiusValid = true
                    )
                }
            }
        }
    }
}