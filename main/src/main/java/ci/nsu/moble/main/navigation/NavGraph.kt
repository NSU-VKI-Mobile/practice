package ci.nsu.moble.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.*
import ci.nsu.moble.main.viewmodel.DepositViewModel
import ci.nsu.moble.main.ui.screens.*

@Composable
fun NavGraph(vm: DepositViewModel) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {

        composable("home") { HomeScreen(navController) }
        composable("step1") { Step1Screen(navController, vm) }
        composable("step2") { Step2Screen(navController, vm) }
        composable("result") { ResultScreen(navController, vm) }
        composable("history") { HistoryScreen(navController, vm) }

        composable("details/{id}") { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getString("id")
                ?.toLongOrNull()

            val item = vm.history.collectAsState(initial = emptyList())
                .value
                .firstOrNull { it.id == id }

            if (item != null) {
                DepositDetailsScreen(
                    navController = navController,
                    vm = vm,
                    item = item
                )
            }
        }
    }
}