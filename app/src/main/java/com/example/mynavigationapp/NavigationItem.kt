package com.example.mynavigationapp

sealed class NavigationItem(
    val route: String,      // уникальное название экрана
    val title: String,      // заголовок
    val icon: Int           // иконка
) {
    // Первый экран
    object Home : NavigationItem(
        route = "home",
        title = "Главная",
        icon = android.R.drawable.ic_menu_gallery
    )

    // Второй экран
    object Profile : NavigationItem(
        route = "profile",
        title = "Профиль",
        icon = android.R.drawable.ic_menu_myplaces
    )

    // Третий экран
    object Settings : NavigationItem(
        route = "settings",
        title = "Настройки",
        icon = android.R.drawable.ic_menu_preferences
    )

    // Список всех экранов для меню
    companion object {
        val items = listOf(Home, Profile, Settings)
    }
}