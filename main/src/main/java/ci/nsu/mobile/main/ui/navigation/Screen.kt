package ci.nsu.mobile.main.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Calculation : Screen("calculation")
    object Additional : Screen("additional")
    object History : Screen("history")
}