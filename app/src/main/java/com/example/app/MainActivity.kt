package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.app.navigation.MainNavigation
import com.example.app.ui.theme.PracticeTheme
import com.example.app.viewmodel.historyDeposits.HistoryDepositsViewModel
import com.example.app.viewmodel.users.UsersViewModel
import com.example.domain.interfaces.AuthManager
import com.example.domain.interfaces.AuthNavigator
import com.example.domain.interfaces.CalculationsNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authManager: AuthManager  // ✅ через @Inject
    @Inject
    lateinit var authNavigator: AuthNavigator
    @Inject
    lateinit var calculationsNavigator: CalculationsNavigator
    private val usersViewModel: UsersViewModel by viewModels()
    private val historyDepositsViewModel: HistoryDepositsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {

                MainNavigation(
                    navController = rememberNavController(),
                    usersViewModel = usersViewModel,
                    historyDepositsViewModel = historyDepositsViewModel,
                    authManager,
                    authNavigator,
                    calculationsNavigator

                )
            }
        }
    }
}