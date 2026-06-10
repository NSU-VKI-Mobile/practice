package ci.nsu.mobile.main.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

enum class BottomNavTab(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    Users(AppRoutes.Main.Users, Icons.Default.Person, "Пользователи"),
    Deposits(AppRoutes.Main.Deposits, Icons.Default.List, "Расчеты"),
    Calculation(AppRoutes.Main.Deposit.Step1, Icons.Default.Create, "Расчет")
}

@Composable
fun BottomBar(
    navController: NavController
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavTab.entries.forEach { tab ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route
                    ?.substringBefore("/")
                    ?.substringBefore("?") == tab.route::class.qualifiedName
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}