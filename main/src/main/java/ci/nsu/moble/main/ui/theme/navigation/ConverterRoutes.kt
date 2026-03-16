package ci.nsu.mobile.main.navigation

sealed class ConverterRoutes(val route: String, val title: String) {
    object CelsiusToFahrenheit : ConverterRoutes("celsius_to_fahrenheit", "Цельсий → Фаренгейт")
    object FahrenheitToCelsius : ConverterRoutes("fahrenheit_to_celsius", "Фаренгейт → Цельсий")

    companion object {
        val bottomNavItems = listOf(CelsiusToFahrenheit, FahrenheitToCelsius)

        fun fromRoute(route: String?): ConverterRoutes {
            return when (route) {
                CelsiusToFahrenheit.route -> CelsiusToFahrenheit
                FahrenheitToCelsius.route -> FahrenheitToCelsius
                else -> CelsiusToFahrenheit
            }
        }
    }
}