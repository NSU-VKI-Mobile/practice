package ci.nsu.mobile.main.navigation

sealed class Routes(val route: String) {
    object MainScreen : Routes("MainScreen")
    object FistScreen : Routes("FirstScreen")
    object SecondScreen : Routes("SecondScreen")
    object ResultScreen: Routes("ResultScreen")
    object HistoryScreen: Routes("HistoryScreen")
}