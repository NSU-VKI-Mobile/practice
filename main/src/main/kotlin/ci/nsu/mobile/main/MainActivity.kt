package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.ui.screen.LoginScreen
import ci.nsu.mobile.main.ui.screen.MainScreen
import ci.nsu.mobile.main.ui.screen.RegisterScreen
import ci.nsu.mobile.main.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        if (TokenManager.token != null) {
            currentScreen = "main"
        }
    }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = { currentScreen = "main" },
            onNavigateToRegister = { currentScreen = "register" }
        )
        "register" -> RegisterScreen(
            onRegisterSuccess = { currentScreen = "login" },
            onNavigateToLogin = { currentScreen = "login" }
        )
        "main" -> MainScreen(
            onLogout = {
                currentScreen = "login"
            }
        )
    }
}