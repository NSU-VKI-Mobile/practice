package ci.nsu.mobile.auth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.auth.ui.LoginScreen
import ci.nsu.mobile.auth.ui.RegisterScreen

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRoute = intent.getStringExtra(EXTRA_START_ROUTE) ?: ROUTE_LOGIN
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthFlow(
                        startRoute = startRoute,
                        onAuthenticated = {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_START_ROUTE = "ci.nsu.mobile.auth.START_ROUTE"
        const val ROUTE_LOGIN = "login"
        const val ROUTE_REGISTER = "register"
    }
}

@Composable
fun AuthFlow(
    startRoute: String = AuthActivity.ROUTE_LOGIN,
    onAuthenticated: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startRoute) {
        composable(AuthActivity.ROUTE_LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(AuthActivity.ROUTE_REGISTER) },
                onLoginSuccess = onAuthenticated
            )
        }

        composable(AuthActivity.ROUTE_REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(AuthActivity.ROUTE_LOGIN) {
                        popUpTo(AuthActivity.ROUTE_REGISTER) { inclusive = true }
                    }
                }
            )
        }
    }
}
