package com.example.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth.ui.screens.LoginScreen
import com.example.auth.ui.screens.RegistrationScreen
import com.example.auth.viewmodel.login.LoginViewModel
import com.example.auth.viewmodel.registration.RegistrationViewModel

sealed class Screens(val route: String) {
    object LoginScreen : Screens("LoginScreen")
    object RegistrationScreen : Screens("RegistrationScreen")
}

@Composable
fun Navigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegistrationViewModel,
    onLoginSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route,
    ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                navTo = { navigateTo -> navController.navigate(navigateTo) },
                viewModel = loginViewModel
            )
        }
        composable(Screens.RegistrationScreen.route) {
            RegistrationScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = { navController.navigate(Screens.LoginScreen.route) }
            )
        }
    }
}