package ci.nsu.mobile.main.core.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ci.nsu.mobile.main.R

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: @Composable () -> Unit
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            route = Routes.USERS,
            title = "Пользователи",
            icon = { Icon(
                painter = painterResource(id = R.drawable.person_icon),
                contentDescription = "Users",
                modifier = Modifier.size(24.dp)
            )}
        ),
        BottomNavItem(
            route = Routes.HISTORY,
            title = "Мои расчёты",
            icon = { Icon(
                painter = painterResource(id = R.drawable.briefcase_portfolio),
                contentDescription = "Portfolio",
                modifier = Modifier.size(24.dp)
            )}
        ),
        BottomNavItem(
            route = Routes.NEW_CALCULATION,
            title = "Новый расчёт",
            icon = { Icon(
                painter = painterResource(id = R.drawable.calculator_icon),
                contentDescription = "Calculate",
                modifier = Modifier.size(24.dp)
            )}
        )
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = item.icon,
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}