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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
                SecondScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen() {

    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current

    val text = (context as Activity)
        .intent
        .getStringExtra("text_data") ?: ""

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(text) },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as Activity).finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }
            )
        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate(Screen.Home.route)
                    },
                    icon = { Icon(Icons.Filled.Home, null) },
                    label = { Text("Главная") }
                )

                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate(Screen.ScreenOne.route)
                    },
                    icon = { Icon(Icons.Filled.List, null) },
                    label = { Text("Второй экран") }
                )
            }
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Home.route) {
                Text("Главная страница")
            }

            composable(Screen.ScreenOne.route) {
                Text("Экран №2")
            }
        }
    }
}