package ci.nsu.moble.main.ui

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Window2 : Screen("window2")
}