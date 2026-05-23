package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.navigation.NavControlFun
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositActivity : ComponentActivity() {
    private lateinit var repository: DepositRepository

    private val depositCalculationViewModel: DepositCalculationViewModel by viewModels()
    private val historyDepositsViewModel: HistoryDepositsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme() {
                NavControlFun(navController = rememberNavController(),
                    depositCalculationViewModel,
                    historyDepositsViewModel)
            }
        }
    }
}