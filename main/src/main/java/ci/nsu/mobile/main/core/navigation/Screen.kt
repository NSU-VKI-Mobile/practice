package ci.nsu.mobile.main.core.navigation

sealed class Screen(val route: String) {
    object Login : Screen("log_in")
    object Register : Screen("register")
    object Main : Screen("main")
}