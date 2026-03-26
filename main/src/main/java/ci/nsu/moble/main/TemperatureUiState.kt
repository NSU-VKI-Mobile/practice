package ci.nsu.moble.main

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val celsiusDouble: Double? get() = celsius.toDoubleOrNull()
    val fahrenheitDouble: Double? get() = fahrenheit.toDoubleOrNull()

    val isCelsiusValid: Boolean get() = celsius.isEmpty() || celsiusDouble != null
    val isFahrenheitValid: Boolean get() = fahrenheit.isEmpty() || fahrenheitDouble != null
}