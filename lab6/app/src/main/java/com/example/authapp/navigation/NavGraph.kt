package com.example.authapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authapp.ui.AuthViewModel
import com.example.authapp.ui.login.LoginScreen
import com.example.authapp.ui.register.RegisterScreen
import com.example.authapp.ui.users.UsersScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLogin = {
                    navController.navigate(Screen.Users.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onRegistered = {
                    viewModel.clearRegistered()
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Users.route) {
            UsersScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Users.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
