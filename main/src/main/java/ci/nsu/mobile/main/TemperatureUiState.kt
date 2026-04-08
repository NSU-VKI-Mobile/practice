package ci.nsu.mobile.main

import androidx.compose.runtime.Stable
import kotlin.text.toDoubleOrNull

@Stable
data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {

    val isCelsiusValid: Boolean
        get() = celsius.toDoubleOrNull() != null

    val isFahrenheitValid: Boolean
        get() = fahrenheit.toDoubleOrNull() != null
}