// Task_6: Главная Activity — Compose-хост с навигацией через sealed class Screen.
package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.MainViewModel
import ci.nsu.moble.main.ui.Screen
import ci.nsu.moble.main.ui.screens.LoginScreen
import ci.nsu.moble.main.ui.screens.RegisterScreen
import ci.nsu.moble.main.ui.screens.UsersScreen
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                val viewModel: MainViewModel = viewModel()
                val screen by viewModel.currentScreen.collectAsStateWithLifecycle()

                when (screen) {
                    is Screen.Login -> LoginScreen(viewModel)
                    is Screen.Register -> RegisterScreen(viewModel)
                    is Screen.Users -> UsersScreen(viewModel)
                }
            }
        }
    }
}
