package ci.nsu.moble.main

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean get() = celsius.isEmpty() || celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.isEmpty() || fahrenheit.toDoubleOrNull() != null
}