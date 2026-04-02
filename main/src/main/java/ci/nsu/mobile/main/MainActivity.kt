package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                val navController = rememberNavController()
                val viewModel: DepositViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "main" // Начинаем с главного экрана
                ) {
                    // Описываем маршруты (экраны создадим в следующем шаге)
                    composable("main") { /* Тут будет MainScreen(navController) */ }
                    composable("step1") { /* Тут будет Step1Screen(navController, viewModel) */ }
                    composable("step2") { /* Тут будет Step2Screen(navController, viewModel) */ }
                    composable("result") { /* Тут будет ResultScreen(navController, viewModel) */ }
                    composable("history") { /* Тут будет HistoryScreen(navController, viewModel) */ }
                }
            }
        }
    }
}
