package ci.nsu.moble.main.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Info : Screen("info", "Информация", Icons.Default.Info)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)
}