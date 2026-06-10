package ci.nsu.mobile.main.ui.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.Step2Screen
import ci.nsu.mobile.main.ui.screens.Step1Screen
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.viewmodel.*

@Composable
fun AppNavHost() {

    val context = LocalContext.current

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val repository = remember {
        DepositRepository(db.depositDao())
    }

    val factory = remember {
        DepositViewModelFactory(repository)
    }

    val vm: DepositViewModel = viewModel(factory = factory)

    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {

        composable("main") { MainScreen(navController) }

        composable("step1") { Step1Screen(navController, vm) }

        composable("step2") { Step2Screen(navController, vm) }

        composable("result") { ResultScreen(navController, vm) }

        composable("history") { HistoryScreen(vm) }
    }
}