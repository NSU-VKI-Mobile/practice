package ci.nsu.mobile.main.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.HomeScreen
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.Step1Screen
import ci.nsu.mobile.main.ui.screens.Step2Screen
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun MainNavGraph (modifier: Modifier = Modifier) {

    val vm : AuthViewModel = hiltViewModel()
    val Deposit_vm : DepositViewModel = hiltViewModel()
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
                LoginScreen(
                    vm = vm,
                    openRegister = {
                        navController.navigate(AppRoutes.Auth.Register)
                    },
                    openHome = {
                        navController.navigate(AppRoutes.Main.Users)
                    }
                )
            }

            composable(AppRoutes.Auth.Register) {
                RegisterScreen(
                    vm = vm,
                    onSuccess = {
                        navController.navigate(AppRoutes.Main.Users)
                    }
                )
            }

            composable(AppRoutes.Main.Users) {
                HomeScreen(vm = vm)
            }

            composable(AppRoutes.Main.Deposit.Step1) { Step1Screen(navController, Deposit_vm) }

            composable(AppRoutes.Main.Deposit.Step2) { Step2Screen(navController, Deposit_vm) }

            composable(AppRoutes.Main.Deposit.Result) { ResultScreen(navController, Deposit_vm) }

            composable(AppRoutes.Main.Deposits) { HistoryScreen(Deposit_vm) }
        }


    }
}