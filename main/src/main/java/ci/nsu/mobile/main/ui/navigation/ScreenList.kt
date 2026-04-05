package ci.nsu.mobile.main.ui.navigation

sealed class ScreenList(val route: String) {
    object Main : ScreenList("main")
    object Calculation : ScreenList("calculation")
    object Additional : ScreenList("additional/{amount}/{term}") {
        fun passArguments(amount: String, term: String): String {
            return "additional/$amount/$term"
        }
    }
    object History : ScreenList("history")
}