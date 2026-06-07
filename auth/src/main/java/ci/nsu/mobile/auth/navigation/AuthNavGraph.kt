package ci.nsu.mobile.auth.navigation

import UsersScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.RegistrationScreen
import ci.nsu.mobile.auth.viewModels.login.LoginViewModel
import ci.nsu.mobile.auth.viewModels.registration.RegistrationViewModel
import ci.nsu.mobile.auth.viewModels.users.UsersViewModel
import ci.nsu.mobile.domain.navigation.Screens

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegistrationViewModel,
    usersViewModel: UsersViewModel,
    onNavigateToHistory: () -> Unit
) {
    composable(Screens.LoginScreen.route) {
        LoginScreen(
            onLoginSuccess = onNavigateToHistory,
            navTo = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = loginViewModel
        )
    }

    composable(Screens.RegistrationScreen.route) {
        RegistrationScreen(
            viewModel = registerViewModel,
            onRegisterSuccess = {
                navController.navigate(Screens.LoginScreen.route) {
                    popUpTo(Screens.RegistrationScreen.route) { inclusive = true }
                }
            }
        )
    }

    composable(Screens.UsersScreen.route) {
        UsersScreen(
            viewModel = usersViewModel
        )
    }
}