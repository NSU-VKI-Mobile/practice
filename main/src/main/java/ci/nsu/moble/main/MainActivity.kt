// Task_5: Главная Activity — Compose NavHost для 6 экранов.
// Навигация через sealed class Screen + ViewModel.

package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Screen
import ci.nsu.moble.main.ui.screens.DetailScreen
import ci.nsu.moble.main.ui.screens.HistoryScreen
import ci.nsu.moble.main.ui.screens.MainScreen
import ci.nsu.moble.main.ui.screens.ResultScreen
import ci.nsu.moble.main.ui.screens.Stage1Screen
import ci.nsu.moble.main.ui.screens.Stage2Screen
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                val viewModel: MainViewModel = viewModel()
                val screen by viewModel.currentScreen.collectAsStateWithLifecycle()

                when (screen) {
                    is Screen.Main -> MainScreen(
                        onCalculate = { viewModel.navigateTo(Screen.Stage1) },
                        onHistory = { viewModel.navigateTo(Screen.History) },
                        onClose = { finishAffinity() }
                    )
                    is Screen.Stage1 -> Stage1Screen(viewModel)
                    is Screen.Stage2 -> Stage2Screen(viewModel)
                    is Screen.Result -> ResultScreen(viewModel)
                    is Screen.History -> HistoryScreen(viewModel)
                    is Screen.Detail -> DetailScreen(viewModel)
                }
            }
        }
    }
}
