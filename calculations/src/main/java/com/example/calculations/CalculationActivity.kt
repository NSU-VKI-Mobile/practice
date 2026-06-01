package com.example.calculations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.calculations.navigation.Navigation
import com.example.calculations.ui.theme.PracticeTheme
import com.example.calculations.viewmodel.DepositCalculationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculationActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getLongExtra("userId", -1L)
        setContent {
            PracticeTheme {
                val navController = rememberNavController()
                val viewModel: DepositCalculationViewModel = viewModel()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Новый расчет") },
                            navigationIcon = {
                                androidx.compose.material3.IconButton(
                                    onClick = { finish() }  // ✅ кнопка "Назад" закрывает Activity
                                ) {
                                    androidx.compose.material3.Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Назад"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Navigation(
                        navController = navController,
                        depositCalculationViewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}