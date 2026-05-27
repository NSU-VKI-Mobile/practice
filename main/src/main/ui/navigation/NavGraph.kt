package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.screens.UserListScreen
import ci.nsu.mobile.main.viewmodel.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object UserList : Screen("userList")
}

@Composable
fun NavGraph(
    authRepository: AuthRepository
) {
    val navController = rememberNavController()
    val isLoggedIn by authRepository.authState.collectAsState()

    val startDestination = if (isLoggedIn == true) {
        Screen.UserList.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.UserList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(authRepository)
            )
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Screen.UserList.route) {
            val viewModel: UserListViewModel = viewModel(
                factory = UserListViewModelFactory(authRepository)
            )
            UserListScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.UserList.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}