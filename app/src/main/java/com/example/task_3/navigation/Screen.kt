package com.example.task_3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Главная",
        icon = Icons.Default.Home
    )

    object Profile : Screen(
        route = "profile",
        title = "Профиль",
        icon = Icons.Default.Person
    )

    object Settings : Screen(
        route = "settings",
        title = "Настройки",
        icon = Icons.Default.Settings
    )

    companion object {
        val items = listOf(Home, Profile, Settings)
    }
}
