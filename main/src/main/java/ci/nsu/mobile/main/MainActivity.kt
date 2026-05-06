package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.Screens.FirstStageScreen
import ci.nsu.mobile.main.Screens.ResultStageScreen
import ci.nsu.mobile.main.Screens.SecondStageScreen
import ci.nsu.mobile.main.Screens.HistoryStageScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    MainScreenActivity(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

sealed class ScreenRoutes(val route: String) {
    object MainActivity : ScreenRoutes("MainActivityScreen")
    object FirstStage : ScreenRoutes("FirstStageScreen")
    object SecondStage : ScreenRoutes("SecondStageScreen")
    object Result : ScreenRoutes("ResultStageScreen")
    object History : ScreenRoutes("HistoryStageScreen")
}


@Composable
fun MainScreenActivity(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: DepositViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.MainActivity.route,
        modifier = modifier
    ) {
        composable(ScreenRoutes.MainActivity.route) {
            MainScreen(navController, modifier)
        }
        composable(ScreenRoutes.FirstStage.route) {
            FirstStageScreen(navController, viewModel, modifier)
        }
        composable(ScreenRoutes.SecondStage.route) {
            SecondStageScreen(navController, viewModel, modifier)
        }
        composable(ScreenRoutes.Result.route){
            ResultStageScreen(navController, viewModel, modifier)
        }
        composable(ScreenRoutes.History.route){
            HistoryStageScreen(navController, viewModel, modifier)
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calculation of deposits")
        // navigation
        Button(
            onClick = {
                navController.navigate(ScreenRoutes.FirstStage.route)
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Calculate")
        }
        Button(
            onClick = {
                navController.navigate(ScreenRoutes.History.route)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Calculation history")
        }
        Button(
            onClick = {
                (context as? Activity)?.finishAffinity()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Close the app")
        }
    }
}