package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.ui.navigation.Screen
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        TokenManager.init(this)

        setContent {

            val navController =
                rememberNavController()

            val vm: AuthViewModel =
                viewModel()

            NavHost(
                navController,
                startDestination =
                    Screen.Login.route
            ) {

                composable(
                    Screen.Login.route
                ) {

                    LoginScreen(
                        vm,
                        {
                            navController.navigate(
                                Screen.Register.route
                            )
                        },
                        {
                            navController.navigate(
                                Screen.Home.route
                            )
                        }
                    )
                }

                composable(
                    Screen.Register.route
                ) {
                    RegisterScreen()
                }

                composable(
                    Screen.Home.route
                ) {
                    HomeScreen(vm)
                }
            }
        }
    }
}