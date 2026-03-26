package ci.nsu.moble.main

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean get() = celsius.isEmpty() || celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.isEmpty() || fahrenheit.toDoubleOrNull() != null

    val convertedToFahrenheit: String
        get() {
            val c = celsius.toDoubleOrNull() ?: return ""
            val f = c * 9 / 5 + 32
            return "%.2f".format(f)
        }

    val convertedToCelsius: String
        get() {
            val f = fahrenheit.toDoubleOrNull() ?: return ""
            val c = (f - 32) * 5 / 9
            return "%.2f".format(c)
        }
}