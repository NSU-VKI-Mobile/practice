package ci.nsu.mobile.main.ui.navigation

sealed class ScreenList(val route: String) {
    object Main : ScreenList("main")
    object Calculation : ScreenList("calculation")
    object Additional : ScreenList("additional/{amount}/{term}") {
        fun passArguments(amount: String, term: String): String {
            return "additional/$amount/$term"
        }
    }

    object Result : ScreenList("result/{amount}/{term}/{rate}/{monthlyAddition}") {
        fun passArguments(amount: Double, term: Int, rate: Double, monthlyAddition: Double): String {
            return "result/$amount/$term/$rate/$monthlyAddition"
        }
    }
    object History : ScreenList("history")
}