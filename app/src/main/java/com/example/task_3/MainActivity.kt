package com.example.task_3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task_3.navigation.Screen
import com.example.task_3.ui.components.BottomNavBar
import com.example.task_3.ui.screens.HomeScreen
import com.example.task_3.ui.screens.ProfileScreen
import com.example.task_3.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen(
                    onNavigateToSecondActivity = { message ->
                        val intent = Intent(this, SecondActivity::class.java).apply {
                            putExtra(EXTRA_MESSAGE, message)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToSecondActivity: (String) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSecondActivity = onNavigateToSecondActivity
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}