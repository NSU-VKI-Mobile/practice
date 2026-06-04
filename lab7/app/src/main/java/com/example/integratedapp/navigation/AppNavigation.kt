package com.example.integratedapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.di.ServiceLocator
import com.example.integratedapp.ui.MainScreen
import com.example.integratedapp.ui.auth.AuthViewModel
import com.example.integratedapp.ui.login.LoginScreen
import com.example.integratedapp.ui.register.RegisterScreen

sealed class AppScreen(val route: String) {
    object Login    : AppScreen("login")
    object Register : AppScreen("register")
    object Main     : AppScreen("main")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Один общий AuthViewModel для логина и регистрации
    val authViewModel: AuthViewModel = viewModel(factory = ServiceLocator.viewModelFactory)

    // Начальный экран — если уже залогинен, идём сразу в Main
    val startDestination = if (SessionManager.isLoggedIn()) AppScreen.Main.route
                           else AppScreen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(AppScreen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoggedIn = {
                    navController.navigate(AppScreen.Main.route) {
                        popUpTo(AppScreen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppScreen.Register.route) }
            )
        }

        composable(AppScreen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegistered = {
                    authViewModel.clearRegistered()
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppScreen.Main.route) {
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(AppScreen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
