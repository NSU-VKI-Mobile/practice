package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Ошибка была здесь, путь исправлен
import ci.nsu.mobile.main.ui.DepositApp
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: DepositViewModel = viewModel()
            DepositApp(viewModel = viewModel)
        }
    }
}