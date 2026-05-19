package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.room.AppDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.navigation.NavControlFun
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModelFactory
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositActivity : ComponentActivity() {
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
            PracticeTheme() {
                val viewModel: DepositCalculationViewModel = viewModel(factory = viewModelFactory)
                val historyViewModel: HistoryDepositsViewModel =
                    viewModel(factory = historyViewModelFactory)
                NavControlFun(navController = rememberNavController(), viewModel, historyViewModel)
            }
        }
    }
}