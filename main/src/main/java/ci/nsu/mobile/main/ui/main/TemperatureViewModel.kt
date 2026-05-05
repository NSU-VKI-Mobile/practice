package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = "",
    val lastEdited: EditedField = EditedField.NONE
) {
    val isCelsiusValid: Boolean
        get() = celsius.toDoubleOrNull() != null

    val isFahrenheitValid: Boolean
        get() = fahrenheit.toDoubleOrNull() != null

    val showCelsiusError: Boolean
        get() = lastEdited == EditedField.CELSIUS
                && celsius.isNotBlank()
                && !isCelsiusValid

    val showFahrenheitError: Boolean
        get() = lastEdited == EditedField.FAHRENHEIT
                && fahrenheit.isNotBlank()
                && !isFahrenheitValid
}

enum class EditedField { NONE, CELSIUS, FAHRENHEIT }

class TemperatureViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(newValue: String) {
        _uiState.update { current ->
            val fahrenheit = newValue.toDoubleOrNull()
                ?.let { String.format("%.2f", it * 9.0 / 5.0 + 32) }
                ?: ""
            current.copy(
                celsius = newValue,
                fahrenheit = fahrenheit
            )
        }
    }

    fun onFahrenheitChanged(newValue: String) {
        _uiState.update { current ->
            val celsius = newValue.toDoubleOrNull()
                ?.let { String.format("%.2f", (it - 32) * 5.0 / 9.0) }
                ?: ""
            current.copy(
                celsius = celsius,
                fahrenheit = newValue
            )
        }
    }

    fun onCelsiusFocused() {
        _uiState.update { it.copy(lastEdited = EditedField.CELSIUS) }
    }

    fun onFahrenheitFocused() {
        _uiState.update { it.copy(lastEdited = EditedField.FAHRENHEIT) }
    }
}