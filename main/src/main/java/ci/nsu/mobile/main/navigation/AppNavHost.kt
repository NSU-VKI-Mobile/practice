package ci.nsu.mobile.main.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import ci.nsu.mobile.main.ui.screens.deposit.DepositViewModel
import ci.nsu.mobile.main.ui.screens.deposit.HistoryScreen
import ci.nsu.mobile.main.ui.screens.deposit.ResultScreen
import ci.nsu.mobile.main.ui.screens.deposit.Step1Screen
import ci.nsu.mobile.main.ui.screens.deposit.Step2Screen
import ci.nsu.mobile.main.ui.screens.home.HomeScreen
import ci.nsu.mobile.main.ui.screens.home.HomeViewModel
import ci.nsu.mobile.main.ui.screens.login.LoginScreen
import ci.nsu.mobile.main.ui.screens.login.LoginViewModel
import ci.nsu.mobile.main.ui.screens.register.RegisterScreen
import ci.nsu.mobile.main.ui.screens.register.RegisterViewModel

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {


    val navController = rememberNavController()
    val bottomBarRoutes = setOf(
        AppRoutes.Main.Users,
        AppRoutes.Main.Deposits,
        AppRoutes.Main.Deposit.Step1,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.Auth.Login,
            modifier = modifier.padding(innerPadding)
        ) {

            composable(AppRoutes.Auth.Login) {
                val viewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onLogin = {
                        navController.navigate(AppRoutes.Main.Users) {
                            popUpTo(AppRoutes.Main.Users)
                        }
                    },
                    onToRegister = {
                        navController.navigate(AppRoutes.Auth.Register)
                    }
                )
            }
            composable(AppRoutes.Auth.Register) {
                val viewModel: RegisterViewModel = hiltViewModel()
                RegisterScreen(
                    viewModel = viewModel,
                    onRegister = {
                        navController.navigate(AppRoutes.Main.Users) {
                            popUpTo(AppRoutes.Main.Users)
                        }
                    },
                    onToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppRoutes.Main.Users) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel
                )
            }
            composable(AppRoutes.Main.Deposit.Step1) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Main.Deposit.Step1)
                }
                val viewModel: DepositViewModel = hiltViewModel(parentEntry)
                Step1Screen(
                    vm = viewModel,
                    onNext = {
                        if (viewModel.amount.isNotEmpty() && viewModel.months.isNotEmpty()) {
                            navController.navigate(AppRoutes.Main.Deposit.Step2)
                        }
                    }
                )
            }
            composable(AppRoutes.Main.Deposit.Step2) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Main.Deposit.Step1)
                }
                val viewModel: DepositViewModel = hiltViewModel(parentEntry)
                Step2Screen(
                    vm = viewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNext = {
                        viewModel.calculate()
                        navController.navigate(AppRoutes.Main.Deposit.Result)
                    }
                )


            }
            composable(AppRoutes.Main.Deposit.Result) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Main.Deposit.Step1)
                }
                val viewModel: DepositViewModel = hiltViewModel(parentEntry)
                ResultScreen(
                    vm = viewModel,
                    onSave = {
                        viewModel.save()
                        navController.navigate(AppRoutes.Main.Deposit.Step1) {
                            popUpTo(AppRoutes.Main.Deposit.Step1) { inclusive = true }
                        }
                        viewModel.reset()
                    }
                )
            }

            composable(AppRoutes.Main.Deposits) {
                val viewModel: DepositViewModel = hiltViewModel()
                HistoryScreen(
                    vm = viewModel
                )
            }

        }
    }
}