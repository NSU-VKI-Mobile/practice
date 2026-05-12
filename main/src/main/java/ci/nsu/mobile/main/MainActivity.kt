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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.sl.ServiceLocator
import ci.nsu.mobile.main.ui.screens.CalcScreen
import ci.nsu.mobile.main.ui.screens.ErrorScreen
import ci.nsu.mobile.main.ui.screens.HistoryCalcScreen
import ci.nsu.mobile.main.ui.screens.Input1Screen
import ci.nsu.mobile.main.ui.screens.Input2Screen
import ci.nsu.mobile.main.ui.screens.LogInScreen
import ci.nsu.mobile.main.ui.screens.RegistryScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.vm.DepositsViewModel
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

sealed class Screen(val route: String) {
    object LogIn : Screen("login")
    object Registry : Screen("registry")
    object Main : Screen("main")
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
    object HistoryCalc : Screen("HistoryCalc")
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

    val token by authViewModel.token.collectAsState()
    val navController = rememberNavController()
    NavHost(
        modifier = modifier
            .padding(start = 20.dp),
        navController = navController,
        startDestination = Screen.LogIn.route
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
                onLogOutClick = {
                    authViewModel.logOut()
                    navController.navigate(Screen.LogIn.route)
                },
                viewModel = authViewModel
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