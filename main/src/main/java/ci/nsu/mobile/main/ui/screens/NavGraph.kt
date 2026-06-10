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
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel
            )
        }

        composable("main") {
            // Передаем depositViewModel внутрь Главного экрана, где будет BottomNavigationView
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                depositViewModel = depositViewModel
            )
        }
    }
}