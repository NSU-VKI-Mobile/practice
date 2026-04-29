package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

// ДОБАВИТЬ sealed class с 3 routes
sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object List : Screen("list", "List")
    object Settings : Screen("settings", "Settings")
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
    // ДОБАВИТЬ nav controller
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }

    // Получаем текущий маршрут для подсветки меню
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(receivedText) }, navigationIcon = {
                IconButton(onClick = {
                    // ДОБАВИТЬ intent и start MainActivity
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
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue, titleContentColor = Color.White
            )
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = currentRoute == Screen.Home.route,
                onClick = {
                    // ДОБАВИТЬ navigate to home screen by navController
                    navController.navigate(Screen.Home.route)
                    selectedItem = 0
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                label = { Text("Screen One") },
                selected = currentRoute == Screen.List.route,
                onClick = {
                    // ДОБАВИТЬ navigate to screen one
                    navController.navigate(Screen.List.route)
                    selectedItem = 1
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                label = { Text("Screen Two") },
                selected = currentRoute == Screen.Settings.route,
                onClick = {
                    // ДОБАВИТЬ navigate to screen two
                    navController.navigate(Screen.Settings.route)
                    selectedItem = 2
                })
        }
    }) { innerPadding ->
        // ДОБАВИТЬ nav graph с 3 экранами
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeContent(receivedText)
            }
            composable(Screen.List.route) {
                ListContent()
            }
            composable(Screen.Settings.route) {
                SettingsContent()
            }
        }
    }
}

// ДОБАВИТЬ экраны для навигации
@Composable
fun HomeContent(receivedText: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏠 Home Screen", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Получено: $receivedText", fontSize = 16.sp, color = Color.Blue)
        }
    }
}

@Composable
fun ListContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("📋 List Screen", fontSize = 24.sp)
    }
}

@Composable
fun SettingsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("⚙️ Settings Screen", fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}