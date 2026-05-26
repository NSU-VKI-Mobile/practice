package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import ci.nsu.mobile.main.Screens.MainScreen
import ci.nsu.mobile.main.Screens.LoginScreen
import ci.nsu.mobile.main.Screens.RegistrationScreen
import ci.nsu.mobile.main.Repository.AuthRepository
import ci.nsu.mobile.main.ViewModel.AuthViewModel
import ci.nsu.mobile.main.Token.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    val repository = remember {
                        AuthRepository()
                    }

                    val viewModel = remember {
                        AuthViewModel(repository)
                    }

                    MainScreenActivity(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

sealed class ScreenRoutes(val route: String) {
    object Main : ScreenRoutes("MainScreen")
    object Login : ScreenRoutes("LoginScreen")
    object Registration : ScreenRoutes("RegistrationScreen")
}

@Composable
fun MainScreenActivity(navController: NavHostController,
                       viewModel: AuthViewModel,
                       modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Login.route,
        modifier = modifier
    ) {
        composable(ScreenRoutes.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(ScreenRoutes.Main.route) {
                        popUpTo(ScreenRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(ScreenRoutes.Registration.route)
                }
            )
        }

        composable(ScreenRoutes.Main.route) {
            MainScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(ScreenRoutes.Login.route) {
                        popUpTo(ScreenRoutes.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(ScreenRoutes.Registration.route) {
            RegistrationScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}