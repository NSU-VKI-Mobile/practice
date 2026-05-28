package com.example.labapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labapi.data.TokenManager
import com.example.labapi.ui.screens.LoginScreen
import com.example.labapi.ui.screens.MainScreen
import com.example.labapi.ui.screens.RegisterScreen
import com.example.labapi.ui.viewmodels.LoginViewModel
import com.example.labapi.ui.viewmodels.MainViewModel
import com.example.labapi.ui.viewmodels.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this) // обязательная инициализация
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoggedIn = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }
        composable("register") {
            val viewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onRegistered = { navController.navigate("login") { popUpTo("register") { inclusive = true } } }
            )
        }
        composable("main") {
            val viewModel: MainViewModel = viewModel()
            MainScreen(
                viewModel = viewModel,
                onLoggedOut = { navController.navigate("login") { popUpTo(0) { inclusive = true } } }
            )
        }
    }
}