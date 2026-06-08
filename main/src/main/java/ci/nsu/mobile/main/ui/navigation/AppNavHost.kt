package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.network.api.RetrofitClient
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.ui.screens.home.HomeScreen
import ci.nsu.mobile.main.ui.screens.home.HomeViewModel
import ci.nsu.mobile.main.ui.screens.login.LoginScreen
import ci.nsu.mobile.main.ui.screens.login.LoginViewModel
import ci.nsu.mobile.main.ui.screens.register.RegisterScreen
import ci.nsu.mobile.main.ui.screens.register.RegisterViewModel

@Composable
fun AppNavHost(
    retrofitClient: RetrofitClient,
    tokenManager: TokenManager,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel() {
                LoginViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    )
                )
            }
            LoginScreen(viewModel, navController)
        }
        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = viewModel() {
                RegisterViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    )
                )
            }
            RegisterScreen(viewModel, navController)
        }
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel() {
                HomeViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    ),
                    tokenManager = tokenManager
                )
            }
            HomeScreen(viewModel, navController)
        }
    }
}