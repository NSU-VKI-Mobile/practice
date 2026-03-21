package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }
    val currentRoute = navController.currentDestination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        receivedText,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Переход обратно в MainActivity через Intent
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
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
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        // Навигация на home screen
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                    label = { Text("Screen One") },
                    selected = currentRoute == Screen.ScreenOne.route,
                    onClick = {
                        // Навигация на screen one
                        navController.navigate(Screen.ScreenOne.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                    label = { Text("Screen Two") },
                    selected = currentRoute == Screen.ScreenTwo.route,
                    onClick = {
                        // Навигация на screen two
                        navController.navigate(Screen.ScreenTwo.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home Screen",
            fontSize = 28.sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ScreenOneContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen One",
            fontSize = 28.sp,
        )
    }
}

@Composable
fun ScreenTwoContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen Two",
            fontSize = 28.sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}