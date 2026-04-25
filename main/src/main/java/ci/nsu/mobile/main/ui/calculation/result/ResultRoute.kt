package ci.nsu.mobile.main.ui.calculation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel
import ci.nsu.mobile.main.ui.navigation.Screen

@Composable
fun ResultRoute(navController: NavController, entry: NavBackStackEntry) {

    val backStackEntry = remember(entry) {
        navController.getBackStackEntry(Screen.Calculation.route)
    }

    val viewModel: CalculationViewModel =
        hiltViewModel(backStackEntry)
    ResultScreen(
        viewModel = viewModel,
        onMain = {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
        },
    )
}