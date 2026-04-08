package ci.nsu.mobile.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Главная", Icons.Filled.Home)
    data object Dashboard : Screen("dashboard", "Дашборд", Icons.Filled.Info)
    data object Profile : Screen("profile", "Профиль", Icons.Filled.Person)
}