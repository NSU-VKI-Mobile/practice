package ci.nsu.moble.main

// Список всех возможных экранов в нижнем меню
sealed class ScreenState {
    object Home : ScreenState()
    object Favorites : ScreenState()
    object Profile : ScreenState()
}