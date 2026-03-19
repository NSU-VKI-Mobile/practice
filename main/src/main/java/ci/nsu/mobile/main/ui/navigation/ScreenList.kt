package ci.nsu.mobile.main.ui.navigation

sealed class ScreenList(val route: String) {
    object Main : ScreenList("main")
    object Calculation : ScreenList("calculation")
    object Additional : ScreenList("additional")
    object History : ScreenList("history")
}