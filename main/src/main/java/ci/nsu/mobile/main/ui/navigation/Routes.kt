package ci.nsu.mobile.main.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Users : Route("users")
}