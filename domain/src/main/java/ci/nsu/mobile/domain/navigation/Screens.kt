package ci.nsu.mobile.domain.navigation

sealed class Screens(val route: String) {
    object LoginScreen: Screens("LoginScreen")
    object RegistrationScreen: Screens("RegistrationScreen")

    object UsersScreen: Screens("UsersScreen")
    object MainScreen: Screens("MainScreen")
    object FirstScreen: Screens("FirstScreen")
    object SecondScreen: Screens("SecondScreen")
    object ResultScreen: Screens("ResultScreen")
    object HistoryScreen: Screens("HistoryScreen")

    companion object {
        val bottomBarScreens = listOf(
            UsersScreen.route,
            HistoryScreen.route,
            MainScreen.route,
            FirstScreen.route,
            SecondScreen.route,
            ResultScreen.route
        )
        val logoutScreens = listOf(UsersScreen.route)
    }
}