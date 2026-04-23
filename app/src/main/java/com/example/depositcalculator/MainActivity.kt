package com.example.depositcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.depositcalculator.presentation.SharedDepositViewModel
import com.example.depositcalculator.presentation.detail.DetailScreen
import com.example.depositcalculator.presentation.first_step.FirstStepScreen
import com.example.depositcalculator.presentation.history.HistoryScreen
import com.example.depositcalculator.presentation.main.MainScreen
import com.example.depositcalculator.presentation.navigation.Screen
import com.example.depositcalculator.presentation.result.ResultScreen
import com.example.depositcalculator.presentation.second_step.SecondStepScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val sharedVm: SharedDepositViewModel = viewModel()
                NavHost(navController, startDestination = Screen.Main.route) {
                    composable(Screen.Main.route) { MainScreen(navController) }
                    composable(Screen.FirstStep.route) { FirstStepScreen(navController, sharedVm) }
                    composable(Screen.SecondStep.route) { SecondStepScreen(navController, sharedVm) }
                    composable(Screen.Result.route) { ResultScreen(navController, sharedVm) }
                    composable(Screen.History.route) { HistoryScreen(navController) }
                    composable(Screen.Detail.route, Screen.Detail.arguments) { backStack ->
                        val id = backStack.arguments?.getLong("id") ?: 0L
                        DetailScreen(id)
                    }
                }
            }
        }
    }
}