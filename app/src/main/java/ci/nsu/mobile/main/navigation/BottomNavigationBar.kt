package ci.nsu.mobile.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator
import ci.nsu.mobile.domain.navigation.Screens

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    navController: NavHostController,
    calculationsNavigator: CalculationsNavigator,
    authManager: AuthManager
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.People, contentDescription = "Пользователи") },
            selected = currentRoute == Screens.UsersScreen.route,
            onClick = {
                if (currentRoute != Screens.UsersScreen.route) {
                    navController.navigate(Screens.UsersScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Money, contentDescription = "Мои расчеты") },
            selected = currentRoute == Screens.HistoryScreen.route,
            onClick = {
                if (currentRoute != Screens.HistoryScreen.route) {
                    authManager.getCurrentUser()?.userId?.toLong()?.let { userId ->
                        calculationsNavigator.navigateToMyCalculations(navController, userId)
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCard, contentDescription = "Новый расчет") },
            selected = currentRoute in listOf(
                Screens.MainScreen.route,
                Screens.FirstScreen.route,
                Screens.SecondScreen.route,
                Screens.ResultScreen.route
            ),
            onClick = {
                if (currentRoute != Screens.MainScreen.route) {
                    authManager.getCurrentUser()?.userId?.toLong()?.let { userId ->
                        calculationsNavigator.navigateToNewCalculation(navController, userId)
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Мой профиль") },
            selected = currentRoute == Screens.UserOwnScreen.route,
            onClick = {
                if (currentRoute != Screens.UserOwnScreen.route) {
                    navController.navigate(Screens.UserOwnScreen.route)
                }
            }
        )
    }
}