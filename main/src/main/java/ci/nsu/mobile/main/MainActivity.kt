package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.LogInScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegistryScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

sealed class Screen(val route: String) {
    object LogIn : Screen("login")
    object Registry : Screen("registry")
    object Main : Screen("main")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.LogIn.route
    ) {
        composable(Screen.LogIn.route) {
            LogInScreen(
                onRegClick = {navController.navigate(Screen.Registry.route) },
                onLogInClick = {navController.navigate(Screen.Main.route) }
            )
        }

        composable(Screen.Registry.route) {
            RegistryScreen(
                onRegClick = {navController.navigate(Screen.Main.route) }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onExitClick = {(context as? Activity)?.finish()}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting()
    }
}