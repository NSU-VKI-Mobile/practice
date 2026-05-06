package ci.nsu.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import ci.nsu.mobile.ui.screens.*
import ci.nsu.mobile.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.utils.TokenManager

class MainActivity : ComponentActivity() {

    private val vm = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)

        setContent {

            val navController = rememberNavController()

            val start = if (TokenManager.token != null) "main" else "login"

            NavHost(navController, startDestination = start) {

                composable("login") {
                    LoginScreen(vm, navController)
                }

                composable("register") {
                    RegisterScreen(vm, navController)
                }

                composable("main") {
                    MainScreen(vm, navController)
                }
            }
        }
    }
}