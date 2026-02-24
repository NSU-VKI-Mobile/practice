#!/usr/bin/env kotlin

package ci.nsu.moble.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class, представляющий пункты нижнего навигационного меню.
 * @param route уникальный маршрут для навигации Compose
 * @param title отображаемое название пункта
 * @param icon иконка пункта
 */
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Домой", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Профиль", Icons.Default.Person)
    object Settings : BottomNavItem("settings", "Настройки", Icons.Default.Settings)
}