package ci.nsu.mobile.main

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null
}