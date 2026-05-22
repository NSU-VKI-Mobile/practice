package ci.nsu.mobile.main

sealed class BottomNavItem(
    val route: String,
    val title: String
) {

    object Home : BottomNavItem(
        route = "home",
        title = "Home"
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile"
    )

    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings"
    )
}