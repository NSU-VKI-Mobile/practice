package ci.nsu.mobile.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.di.LocalRepository
import ci.nsu.mobile.main.ui.navigation.NavGraph
import ci.nsu.mobile.main.ui.theme.DepositCalculatorTheme
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel.Factory
val LocalDepositViewModel = staticCompositionLocalOf<DepositViewModel> {
    error("No DepositViewModel provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val repository = DepositRepository(database.depositDao())
        // Создаём общий экземпляр DepositViewModel
        val depositViewModel = DepositViewModel(repository)

        setContent {
            DepositCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(
                        LocalRepository provides repository,
                        LocalDepositViewModel provides depositViewModel
                    ) {
                        NavGraph()
                    }
                }
            }
        }
    }
}