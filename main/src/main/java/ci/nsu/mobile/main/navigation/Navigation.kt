package ci.nsu.mobile.main.navigation

import UsersScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import ci.nsu.mobile.main.ui.authScreens.LoginScreen
import ci.nsu.mobile.main.ui.authScreens.RegistrationScreen
import ci.nsu.mobile.main.ui.depositScreens.FirstScreenContent
import ci.nsu.mobile.main.ui.depositScreens.HistoryScreenContent
import ci.nsu.mobile.main.ui.depositScreens.MainScreenContent
import ci.nsu.mobile.main.ui.depositScreens.ResultScreenContent
import ci.nsu.mobile.main.ui.depositScreens.SecondScreenContent
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import ci.nsu.mobile.main.viewmodel.login.LoginViewModel
import ci.nsu.mobile.main.viewmodel.registration.RegistrationViewModel
import ci.nsu.mobile.main.viewmodel.users.UsersViewModel

sealed class Screens(val route: String) {
    object LoginScreen: Screens("LoginScreen")
    object RegistrationScreen: Screens("RegistrationScreen")
    object UsersScreen: Screens("UsersScreen")
    object MainScreen: Screens("MainScreen")
    object FirstScreen: Screens("FirstScreen")
    object SecondScreen: Screens("SecondScreen")
    object ResultScreen: Screens("ResultScreen")
    object HistoryScreen: Screens("HistoryScreen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegistrationViewModel,
    usersViewModel: UsersViewModel,
    historyDepositsViewModel: HistoryDepositsViewModel,
    depositCalculationViewModel: DepositCalculationViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screens.UsersScreen.route,
        Screens.HistoryScreen.route,
        Screens.MainScreen.route,
        Screens.FirstScreen.route,
        Screens.SecondScreen.route,
        Screens.ResultScreen.route
    )

    val showLogOut = currentRoute == Screens.UsersScreen.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(Icons.Filled.People, contentDescription = "Пользователи")
                        },
                        label = { Text("Пользователи") },
                        selected = currentRoute == Screens.UsersScreen.route,
                        onClick = {
                            if (currentRoute != Screens.UsersScreen.route) {
                                navController.navigate(Screens.UsersScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(Icons.Filled.Money, contentDescription = "Мои расчеты")
                        },
                        label = { Text("Мои расчеты") },
                        selected = currentRoute == Screens.HistoryScreen.route,
                        onClick = {
                            if (currentRoute != Screens.HistoryScreen.route) {
                                navController.navigate(Screens.HistoryScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(Icons.Filled.AddCard, contentDescription = "Новый расчет")
                        },
                        label = { Text("Новый расчет") },
                        selected = currentRoute in listOf(
                            Screens.MainScreen.route,
                            Screens.FirstScreen.route,
                            Screens.SecondScreen.route,
                            Screens.ResultScreen.route
                        ),
                        onClick = {
                            if (currentRoute != Screens.MainScreen.route) {
                                navController.navigate(Screens.MainScreen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                    Text("РАСЧЕТ ВКЛАДОВ") },
                actions = {
                    if (showLogOut) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.LoginScreen.route)
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.Logout, contentDescription = "Выйти"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.LoginScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.LoginScreen.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screens.UsersScreen.route) {
                            popUpTo(Screens.LoginScreen.route) { inclusive = true }
                        }
                    },
                    navTo = { navigateTo -> navController.navigate(navigateTo) },
                    viewModel = loginViewModel
                )
            }
            composable(Screens.RegistrationScreen.route) {
                RegistrationScreen(
                    viewModel = registerViewModel,
                    onRegisterSuccess = { navController.navigate(Screens.LoginScreen.route) }
                )
            }
            composable(Screens.UsersScreen.route) {
                UsersScreen(
                    navTo = { navigateTo -> navController.navigate(navigateTo) },
                    viewModel = usersViewModel
                )
            }
            composable(Screens.HistoryScreen.route) {
                HistoryScreenContent(
                    { navigateTo -> navController.navigate(navigateTo) },
                    historyDepositsViewModel
                )
            }
            composable(Screens.MainScreen.route) {
                MainScreenContent( { navigateTo -> navController.navigate(navigateTo) },
                    depositCalculationViewModel
                )
            }
            composable(Screens.FirstScreen.route) {
                FirstScreenContent(
                    { navigateTo -> navController.navigate(navigateTo) },
                    depositCalculationViewModel
                )
            }
            composable(Screens.SecondScreen.route) {
                SecondScreenContent(
                    { navigateTo -> navController.navigate(navigateTo) },
                    depositCalculationViewModel
                )
            }
            composable(Screens.ResultScreen.route) {
                ResultScreenContent(
                    { navigateTo -> navController.navigate(navigateTo) },
                    depositCalculationViewModel
                )
            }
        }
    }
}