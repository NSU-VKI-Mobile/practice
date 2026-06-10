package ci.nsu.mobile.main.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.LoginViewModel
import ci.nsu.mobile.main.ui.viewmodel.RegisterViewModel
import ci.nsu.mobile.main.ui.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    mainViewModel: MainViewModel,
    depositViewModel: DepositViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // маршруты из лабы 6

        composable("login") {
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                depositViewModel = depositViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel
            )
        }

        composable("main") {
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                depositViewModel = depositViewModel
            )
        }



        // маршруты из лабы 5

        composable("deposit_menu") {
            DepositMenuScreen(navController = navController)
        }

        composable("step1") {
            Step1Screen(
                navController = navController,
                viewModel = depositViewModel
            )
        }

        composable("step2") {
            Step2Screen(
                navController = navController,
                viewModel = depositViewModel
            )
        }

        composable("result") {
            ResultScreen(
                navController = navController,
                viewModel = depositViewModel
            )
        }
    }
}