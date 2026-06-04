package ci.nsu.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.runtime.*

import ci.nsu.mobile.di.ServiceLocator
import ci.nsu.mobile.ui.screens.LoginScreen
import ci.nsu.mobile.ui.screens.MainScreen
import ci.nsu.mobile.ui.screens.RegisterScreen
import ci.nsu.mobile.ui.viewmodel.AppViewModelFactory
import ci.nsu.mobile.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.utils.TokenManager

class MainActivity : ComponentActivity() {

    private lateinit var serviceLocator: ServiceLocator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)
        serviceLocator = ServiceLocator(this)

        setContent {

            val navController = rememberNavController()
            val authVm: AuthViewModel = viewModel()

            val depositVm: DepositViewModel = viewModel(
                factory = AppViewModelFactory(
                    serviceLocator.depositRepository
                )
            )

            LaunchedEffect(authVm.isLoggedIn) {
                if (!authVm.isLoggedIn) {

                    depositVm.clearHistory(TokenManager.userLogin)

                    navController.navigate("login") {
                        popUpTo(0)
                    }
                } else {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                composable("login") {
                    LoginScreen(vm = authVm, navController = navController)
                }

                composable("register") {
                    RegisterScreen(vm = authVm, navController = navController)
                }

                composable("main") {
                    MainScreen(
                        authVm = authVm,
                        depositVm = depositVm,
                        userLogin = TokenManager.userLogin
                    )
                }
            }
        }
    }
}