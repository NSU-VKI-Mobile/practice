// Task_3: Sealed class — список возможных состояний для навигации по нижнему меню.

package ci.nsu.moble.main.nvgs

import ci.nsu.moble.main.R

sealed class NavDestination(val title: String, val iconRes: Int) {
    data object Home : NavDestination("Home", R.drawable.ic_home)
    data object Profile : NavDestination("Profile", R.drawable.ic_profile)
    data object Settings : NavDestination("Settings", R.drawable.ic_settings)

    companion object {
        val items = listOf(Home, Profile, Settings)
    }
}
