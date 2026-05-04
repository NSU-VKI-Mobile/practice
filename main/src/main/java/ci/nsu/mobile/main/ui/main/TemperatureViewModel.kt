package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean
        get() = celsius.toDoubleOrNull() != null

    val isFahrenheitValid: Boolean
        get() = fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()
}