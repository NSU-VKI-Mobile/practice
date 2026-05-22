package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {

            NavigationBar {

                val currentRoute = navController.currentBackStackEntryFlow

                items.forEach { item ->

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(item.title)
                        },
                        icon = {}
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Home.route) {
                ScreenContent("Home Screen")
            }

            composable(BottomNavItem.Profile.route) {
                ScreenContent("Profile Screen")
            }

            composable(BottomNavItem.Settings.route) {
                ScreenContent("Settings Screen")
            }
        }
    }
}

@Composable
fun ScreenContent(text: String) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text)
    }
}