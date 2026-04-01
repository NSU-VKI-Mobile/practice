package ci.nsu.moble.main

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = "",
    val isCelsiusEntry: Boolean = true // true — вводим Цельсии, false — Фаренгейты
) {
    val isCelsiusValid: Boolean get() = celsius.isEmpty() || celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.isEmpty() || fahrenheit.toDoubleOrNull() != null
}