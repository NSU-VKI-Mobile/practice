package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import ci.nsu.moble.main.ui.Screens.HomeScreen
import ci.nsu.moble.main.ui.Screens.ScreenOneContent
import ci.nsu.moble.main.ui.Screens.ScreenTwoContent
import ci.nsu.moble.main.ui.theme.PracticeTheme

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ScreenOne : Screen("screen_one")
    object ScreenTwo : Screen("screen_two")
}

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                SecondActivityScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondActivityScreen() {
    val navController = rememberNavController()

    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("EXTRA_DATA") ?: "No text received"
        receivedText += context.intent.getIntExtra("Extra2", 0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(receivedText) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (context is Activity) {
                            context.finish()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },

                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                    label = { Text("Screen One") },
                    selected = currentRoute == Screen.ScreenOne.route,
                    onClick = {
                        navController.navigate(Screen.ScreenOne.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                    label = { Text("Screen Two") },
                    selected = currentRoute == Screen.ScreenTwo.route,
                    onClick = {
                        navController.navigate(Screen.ScreenTwo.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.ScreenOne.route) { ScreenOneContent() }
            composable(Screen.ScreenTwo.route) { ScreenTwoContent() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}