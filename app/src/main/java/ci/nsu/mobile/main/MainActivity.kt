package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.auth.di.AuthNavigatorImpl
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.RegisterScreen
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.calculations.di.CalculationsNavigatorImpl
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.calculations.viewmodel.MyCalculationsViewModel
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.SplashScreen
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    val serviceLocator = remember { ServiceLocator(context.applicationContext) }

    val authManager = serviceLocator.authManager
    val userRepository = serviceLocator.userRepository
    val authRepository = serviceLocator.authRepository
    val depositRepository = serviceLocator.depositRepository
    val userPreferences = serviceLocator.userPreferences

    val authViewModel: AuthViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AuthViewModel(authRepository) }
        }
    )

    val usersViewModel: UsersViewModel = viewModel(
        factory = viewModelFactory {
            initializer { UsersViewModel(userRepository) }
        }
    )

    val depositViewModel: DepositViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DepositViewModel(
                    repository = depositRepository,
                    userIdProvider = { userPreferences.getUserId() ?: -1L }
                )
            }
        }
    )

    val myCalculationsViewModel: MyCalculationsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MyCalculationsViewModel(
                    repository = depositRepository,
                    userIdProvider = { userPreferences.getUserId() ?: -1L }
                )
            }
        }
    )



    var shouldLogout by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLogout) {
        if (shouldLogout) {
            authManager.logout()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
            shouldLogout = false
        }
    }

    val authNavigator = AuthNavigatorImpl(navController)
    val calculationsNavigator = CalculationsNavigatorImpl(navController)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                authManager = authManager,
                onAuthenticated = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        composable("main") {
            MainScreen(
                usersViewModel = usersViewModel,
                depositViewModel = depositViewModel,
                myCalculationsViewModel = myCalculationsViewModel
            )
        }
    }
}