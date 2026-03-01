package com.example.practicenow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicenow.data.local.TokenManager
import com.example.practicenow.ui.login.LoginScreen
import com.example.practicenow.ui.main.MainScreen
import com.example.practicenow.ui.register.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val startDest = if (TokenManager.token != null) "main" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}