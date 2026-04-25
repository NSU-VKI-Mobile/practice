package ci.nsu.mobile.main.ui.calculation.step1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.calculation.CalculationViewModel
import ci.nsu.mobile.main.ui.navigation.Screen

@Composable
fun Step1Route(navController: NavController, entry: NavBackStackEntry) {

    val backStackEntry = remember(entry) {
        navController.getBackStackEntry(Screen.Calculation.route)
    }

    val viewModel: CalculationViewModel =
        hiltViewModel(backStackEntry)

    Step1Screen(
        viewModel = viewModel,
        onNext = { navController.navigate(Screen.Step2.route) },
        onBack = { navController.popBackStack() },
    )
}