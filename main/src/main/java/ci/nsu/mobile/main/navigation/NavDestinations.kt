package ci.nsu.mobile.main.navigation

import ci.nsu.mobile.main.R  // Импортируйте свои ресурсы!

sealed class NavDestinations(val route: String, val title: String, val icon: Int) {

    object Main : NavDestinations(
        route = "main_fragment",
        title = "Главная",
        icon = R.drawable.ic_home  // ← используйте свою иконку
    )

    object Second : NavDestinations(
        route = "second_fragment",
        title = "Вторая",
        icon = R.drawable.ic_favorite  // ← используйте свою иконку
    )

    object Third : NavDestinations(
        route = "third_fragment",
        title = "Третья",
        icon = R.drawable.ic_settings  // ← используйте свою иконку
    )

    companion object {
        val items = listOf(Main, Second, Third)
    }
}