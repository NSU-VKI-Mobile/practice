package ci.nsu.mobile.main.core.navigation

sealed class AuthRoute(val route: String) {
    object Login : AuthRoute("login")
    object Register : AuthRoute("register")
}

sealed class MainRoute(val route: String) {
    object Users : MainRoute("users")
    object History : MainRoute("history")
    object NewCalculation : MainRoute("new_calculation")
}

object Routes {
    const val AUTH_GRAPH = "auth"
    const val MAIN_GRAPH = "main"

    // Auth routes
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Main routes
    const val USERS = "users"
    const val HISTORY = "history"
    const val NEW_CALCULATION = "new_calculation"
    const val ADDITIONAL = "additional/{amount}/{term}"
    const val RESULT = "result/{amount}/{term}/{rate}/{monthlyAddition}"

    // Helper functions to build routes with arguments
    fun additional(amount: String, term: String) = "additional/$amount/$term"
    fun result(amount: Double, term: Int, rate: Double, monthlyAddition: Double) =
        "result/$amount/$term/$rate/$monthlyAddition"
}