package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

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

    val receivedText = if (context is Activity) {
        context.intent.getStringExtra("text_data") ?: "No text received"
    } else ""

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(receivedText) },
                navigationIcon = {
                    IconButton(onClick = {
                        // Возвращаемся в MainActivity
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
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        selectedItem = 0
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                    label = { Text("Screen One") },
                    selected = selectedItem == 1,
                    onClick = {
                        navController.navigate(Screen.ScreenOne.route)
                        selectedItem = 1
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                    label = { Text("Screen Two") },
                    selected = selectedItem == 2,
                    onClick = {
                        navController.navigate(Screen.ScreenTwo.route)
                        selectedItem = 2
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
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.ScreenOne.route) {
                ScreenOneContent()
            }
            composable(Screen.ScreenTwo.route) {
                ScreenTwoContent()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Home Screen", fontSize = 24.sp)
    }
}

@Composable
fun ScreenOneContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Screen One", fontSize = 24.sp)
    }
}

@Composable
fun ScreenTwoContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Screen Two", fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SecondActivityPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}