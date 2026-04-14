package ci.nsu.mobile.main

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
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModelFactory
import ci.nsu.mobile.main.viewmodel.HistoryDepositsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var repository: DepositRepository
    private lateinit var viewModelFactory: DepositCalculationViewModelFactory
    private lateinit var historyViewModelFactory: DepositCalculationViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.Companion.getDatabase(this)
        repository = DepositRepository(database.depositDao())
        viewModelFactory = DepositCalculationViewModelFactory(repository)
        historyViewModelFactory = DepositCalculationViewModelFactory(repository)

        setContent {
            AppTheme {
                val viewModel: DepositCalculationViewModel = viewModel(factory = viewModelFactory)
                val historyViewModel: HistoryDepositsViewModel =
                    viewModel(factory = historyViewModelFactory)
                NavControlFun(navController = rememberNavController(), viewModel, historyViewModel)
            }
        }
    }
}