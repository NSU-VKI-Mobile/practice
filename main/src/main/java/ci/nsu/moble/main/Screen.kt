package ci.nsu.moble.main

import androidx.annotation.StringRes

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int
) {
    object Home : Screen("home", R.string.home)
    object ScreenOne : Screen("screen_one", R.string.screen_one)
    object ScreenTwo : Screen("screen_two", R.string.screen_two)
}