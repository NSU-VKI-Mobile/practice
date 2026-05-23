package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.navigation.Navigation
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import ci.nsu.mobile.main.viewmodel.login.LoginViewModel
import ci.nsu.mobile.main.viewmodel.registration.RegistrationViewModel
import ci.nsu.mobile.main.viewmodel.users.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private val historyDepositsViewModel: HistoryDepositsViewModel by viewModels()
    private val depositCalculationViewModel: DepositCalculationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Navigation(navController = rememberNavController(),
                    loginViewModel = loginViewModel,
                    registerViewModel = registrationViewModel,
                    usersViewModel = usersViewModel,
                    historyDepositsViewModel = historyDepositsViewModel,
                    depositCalculationViewModel = depositCalculationViewModel
                )
            }
        }
    }
}
