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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
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
    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }

    if (context is Activity) {
        receivedText =
            context.intent.getStringExtra(MainActivity.EXTRA_TEXT) ?: "No text received"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        // TOP BAR
        topBar = {
            TopAppBar(
                title = { Text(receivedText) },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)

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

        // BOTTOM NAVIGATION
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate(Screens.Home.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "First") },
                    label = { Text("Screen One") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate(Screens.First.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Second") },
                    label = { Text("Screen Two") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate(Screens.Second.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

    ) { innerPadding ->

        // NAV HOST
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screens.Home.route) {
                Text("Home Screen", fontSize = 30.sp)
            }

            composable(Screens.First.route) {
                Text("Screen One", fontSize = 30.sp)
            }

            composable(Screens.Second.route) {
                Text("Screen Two", fontSize = 30.sp)
            }
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