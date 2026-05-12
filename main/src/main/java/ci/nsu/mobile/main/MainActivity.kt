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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.sl.ServiceLocator
import ci.nsu.mobile.main.ui.screens.CalcScreen
import ci.nsu.mobile.main.ui.screens.HistoryCalcScreen
import ci.nsu.mobile.main.ui.screens.Input1Screen
import ci.nsu.mobile.main.ui.screens.Input2Screen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.vm.DepositsViewModel
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
    object HistoryCalc : Screen("HistoryCalc")
    object LogIn : Screen("login")
    object Registry : Screen("registry")
    object Main2 : Screen("main")
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
    var selectedTab by remember { mutableIntStateOf(0) }

    val authViewModel: LoginAndRegViewModel = viewModel(
        factory = serviceLocator.viewModelFactory
    )

    val depositViewModel: DepositsViewModel = viewModel(
        factory = serviceLocator.viewModelFactory
    )

    val token by authViewModel.token.collectAsState()
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onCalculateClick = { navController.navigate(Screen.Input1.route) },
                onHistoryClick = { navController.navigate(Screen.HistoryCalc.route) },
                onCloseClick = {(context as? Activity)?.finish()}
            )
        }

        composable(Screen.Input1.route) {
            Input1Screen(
                onBackClick = { navController.popBackStack(Screen.Main.route, inclusive = false) },
                onNextClick = {navController.navigate(Screen.Input2.route) },
                viewModel = depositViewModel
            )
        }

        composable(Screen.Input2.route) {
            Input2Screen(
                onBackClick = { navController.popBackStack() },
                onCalcClick = { navController.navigate(Screen.Calc.route) },
                viewModel = depositViewModel
            )
        }

        composable(Screen.Calc.route) {
            CalcScreen(
                onMainClick = { navController.popBackStack(Screen.Main.route, inclusive = false) },
                viewModel = depositViewModel
            )
        }

        composable(Screen.HistoryCalc.route) {
            HistoryCalcScreen(
                onBackClick = { navController.popBackStack()}
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