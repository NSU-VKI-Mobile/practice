package ci.nsu.mobile.main

sealed class BottomNavScreen(val route: String, val label: String) {
    object Home : BottomNavScreen("home", "Home")
    object Profile : BottomNavScreen("profile", "Profile")
    object Settings : BottomNavScreen("settings", "Settings")
}