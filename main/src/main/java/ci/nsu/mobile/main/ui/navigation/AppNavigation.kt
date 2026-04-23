package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.DepositViewModelFactory
import ci.nsu.mobile.main.ui.Screen
import ci.nsu.mobile.main.ui.screens.HistoryDetailScreen
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.Stage1Screen
import ci.nsu.mobile.main.ui.screens.Stage2Screen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val database = AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current)
    val repository = DepositRepository(database.depositDao())
    // Создаём ViewModel с фабрикой, которая передаёт repository
    val viewModel: DepositViewModel = viewModel(
        factory = DepositViewModelFactory(repository)
    )

    val currentScreen by viewModel.currentScreen.collectAsState()
    // При изменении _currentScreen, Compose автоматически перерисует UI
    when (currentScreen) {
        is Screen.Main -> MainScreen(viewModel, modifier)
        is Screen.Stage1 -> Stage1Screen(viewModel, modifier)
        is Screen.Stage2 -> Stage2Screen(viewModel, modifier)
        is Screen.Result -> ResultScreen(viewModel, modifier)
        is Screen.History -> HistoryScreen(viewModel, modifier)
        is Screen.HistoryDetail -> HistoryDetailScreen(viewModel, modifier)
    }
}