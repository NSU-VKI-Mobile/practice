package ci.nsu.mobile.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.domain.DepositRepository
import ci.nsu.mobile.main.navigation.NavControlFun
import ci.nsu.mobile.main.presentation.ui.theme.AppTheme
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModel
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var repository: DepositRepository
    private lateinit var viewModelFactory: DepositCalculationViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)
        repository = DepositRepository(database.depositDao())
        viewModelFactory = DepositCalculationViewModelFactory(repository)
        setContent {
            AppTheme {
                val viewModel: DepositCalculationViewModel = viewModel(factory = viewModelFactory)
                NavControlFun(navController = rememberNavController(),viewModel)
            }
        }
    }
}