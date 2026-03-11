package ci.nsu.mobile.main.navigation

// Sealed class для представления состояний навигации
sealed class NavigationState(val route: String) {
    object Home : NavigationState("home")
    object Profile : NavigationState("profile")
    object Settings : NavigationState("settings")
    object Details : NavigationState("details")
}