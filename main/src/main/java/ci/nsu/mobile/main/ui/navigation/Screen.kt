package ci.nsu.mobile.main.ui.navigation

sealed class Screen(val route: String) {

    data object Login : Screen("login")

    data object Register : Screen("register")

    data object Home : Screen("home")
}