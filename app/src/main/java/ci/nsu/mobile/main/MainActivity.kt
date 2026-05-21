package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.sl.ServiceLocator
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import com.example.auth.manager.AuthManagerImpl
import com.example.auth.ui.ErrorScreen
import com.example.auth.ui.RegistryScreen
import com.example.auth.ui.LogInScreen
import com.example.auth.vm.LoginAndRegViewModel
import com.example.calculations.vm.DepositsViewModel
import com.example.domain.interfaces.AuthManager

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
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val serviceLocator = remember { ServiceLocator.getInstance(context) }

    val authViewModel: LoginAndRegViewModel = viewModel(
        factory = serviceLocator.viewModelFactory
    )

    val depositViewModel: DepositsViewModel = viewModel(
        factory = serviceLocator.viewModelFactory
    )

    val authManager: AuthManager = AuthManagerImpl()
    val navController = rememberNavController()
    NavHost(
        modifier = modifier
            .padding(start = 20.dp),
        navController = navController,
        startDestination = if(authManager.isLoggedIn()) Screen.Main.route else Screen.LogIn.route
    ) {
        composable(Screen.LogIn.route) {
            LogInScreen(
                onRegClick = {navController.navigate(Screen.Registry.route) },
                onLogInClick = { authViewModel.logIn({navController.navigate(Screen.Registry.route)})},
                onExitClick = {(context as? Activity)?.finish()},
                viewModel = authViewModel
            )
        }

        composable(Screen.Registry.route) {
            RegistryScreen(
                onRegClick = {
                    authViewModel.registry()
                    navController.navigate(Screen.LogIn.route) },
                onBackClick = {navController.popBackStack()},
                viewModel = authViewModel
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                depositsViewModel = depositViewModel,
                authViewModel = authViewModel,
                authManager = authManager,
                onBackClick = {authManager.logout(); navController.navigate(Screen.LogIn.route)}
            )
        }
    }

    authViewModel.errorMessage?.let{e ->
        ErrorScreen(
            onDismiss = {authViewModel.errorMessage = null},
            onExit = {(context as? Activity)?.finish()},
            error = e)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting()
    }
}