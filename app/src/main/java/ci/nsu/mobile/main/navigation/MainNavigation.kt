package ci.nsu.mobile.main.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.auth.navigation.authNavGraph
import ci.nsu.mobile.auth.viewModels.login.LoginViewModel
import ci.nsu.mobile.auth.viewModels.registration.RegistrationViewModel
import ci.nsu.mobile.auth.viewModels.userOwn.UserOwnViewModel
import ci.nsu.mobile.auth.viewModels.users.UsersViewModel
import ci.nsu.mobile.calculations.navigation.calculationsNavGraph
import ci.nsu.mobile.calculations.viewModels.deposit.DepositCalculationViewModel
import ci.nsu.mobile.calculations.viewModels.historyDeposits.HistoryDepositsViewModel
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.AuthNavigator
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator
import ci.nsu.mobile.domain.navigation.BottomNavManagerImpl
import ci.nsu.mobile.domain.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
    authNavigator: AuthNavigator,
    calculationsNavigator: CalculationsNavigator,
    authManager: AuthManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val loginViewModel: LoginViewModel = hiltViewModel()
    val registerViewModel: RegistrationViewModel = hiltViewModel()
    val usersViewModel: UsersViewModel = hiltViewModel()
    val historyDepositsViewModel: HistoryDepositsViewModel = hiltViewModel()
    val depositCalculationViewModel: DepositCalculationViewModel = hiltViewModel()
    val userOwnViewModel: UserOwnViewModel = hiltViewModel()

    val bottomNavManager = remember { BottomNavManagerImpl() }
    val showBottomBar = bottomNavManager.isBottomBarVisible(currentRoute)
    val showLogOut = bottomNavManager.isLogoutVisible(currentRoute)

    LaunchedEffect(authManager.isLoggedIn()) {
        if (!authManager.isLoggedIn()) {
            authNavigator.navigateToLogin(navController)
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    navController = navController,
                    calculationsNavigator = calculationsNavigator,
                    authManager = authManager
                )
            }
        },
        topBar = {
            if (showBottomBar) {
                CenterAlignedTopAppBar(
                    title = { Text("РАСЧЕТ ВКЛАДОВ") },
                    actions = {
                        if (showLogOut) {
                            IconButton(
                                onClick = {
                                    authManager.logout()
                                    authNavigator.navigateToLogin(navController)
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.Logout,
                                    contentDescription = "Выйти"
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.LoginScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            authNavGraph(
                navController = navController,
                loginViewModel = loginViewModel,
                registerViewModel = registerViewModel,
                usersViewModel = usersViewModel,
                userOwnViewModel = userOwnViewModel,
                onNavigateToHistory = {
                    navController.navigate(Screens.HistoryScreen.route) {
                        popUpTo(Screens.LoginScreen.route) { inclusive = true }
                    }
                }
            )
            calculationsNavGraph(
                navController = navController,
                historyDepositsViewModel = historyDepositsViewModel,
                depositCalculationViewModel = depositCalculationViewModel
            )
        }
    }
}