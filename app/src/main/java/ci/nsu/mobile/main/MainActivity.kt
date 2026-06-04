package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.ui.AuthViewModel
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.RegisterScreen
import ci.nsu.mobile.calculations.ui.DepositViewModel
import ci.nsu.mobile.main.ui.AppViewModelFactory
import ci.nsu.mobile.main.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        ServiceLocator.init(this)
        setContent { AppRoot() }
    }
}

@Composable
fun AppRoot() {
    val factory = remember { AppViewModelFactory(ServiceLocator.get()) }
    val authVm: AuthViewModel = viewModel(factory = factory)
    val depositVm: DepositViewModel = viewModel(factory = factory)

    val nav = rememberNavController()
    val start = if (TokenManager.token != null) "main" else "login"

    NavHost(navController = nav, startDestination = start) {

        composable("login") {
            LoginScreen(vm = authVm, onLoginSuccess = {
                nav.navigate("main") { popUpTo("login") { inclusive = true } }
            }, onRegisterClick = { nav.navigate("register") })
        }

        composable("register") {
            RegisterScreen(vm = authVm, onSuccess = { nav.popBackStack() })
        }

        composable("main") {
            MainScreen(
                authVm = authVm, depositVm = depositVm, onLogout = {
                    authVm.logout()
                    nav.navigate("login") { popUpTo("main") { inclusive = true } }
                })
        }
    }
}