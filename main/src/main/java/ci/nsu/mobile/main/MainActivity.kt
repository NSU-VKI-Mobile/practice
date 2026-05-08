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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.api.TokenManager
import ci.nsu.mobile.main.ui.screens.ErrorScreen
import ci.nsu.mobile.main.ui.screens.LogInScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegistryScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

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
fun Greeting(modifier: Modifier = Modifier, viewModel: LoginAndRegViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val token by viewModel.token.collectAsState()
    NavHost(
        modifier = modifier
            .padding(start = 20.dp),
        navController = navController,
        startDestination = Screen.LogIn.route
    ) {
        composable(Screen.LogIn.route) {
            LogInScreen(
                onRegClick = {navController.navigate(Screen.Registry.route) },
                onLogInClick = { viewModel.logIn() },
                onExitClick = {(context as? Activity)?.finish()},
                viewModel = viewModel
            )
        }

        composable(Screen.Registry.route) {
            RegistryScreen(
                onRegClick = {
                    viewModel.registry()
                    navController.navigate(Screen.LogIn.route) },
                onBackClick = {navController.popBackStack()},
                viewModel = viewModel
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onLogOutClick = {
                    viewModel.logOut()
                    navController.navigate(Screen.LogIn.route)
                },
                viewModel = viewModel
            )
        }
    }

    viewModel.errorMessage?.let{e ->
        ErrorScreen(
            onDismiss = {viewModel.errorMessage = null},
            onExit = {(context as? Activity)?.finish()},
            error = e)
    }

    LaunchedEffect(token){
        if(token != null) {
            navController.navigate(Screen.Main.route)
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