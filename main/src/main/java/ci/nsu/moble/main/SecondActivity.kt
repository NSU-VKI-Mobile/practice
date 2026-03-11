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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
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

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current

    var receivedText by remember { mutableStateOf("") }

    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
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

                NavigationBarItem(

                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },

                    label = { Text("Home") },

                    selected = selectedItem == 0,

                    onClick = {

                        selectedItem = 0

                        navController.navigate(Screen.Home.route)

                    }

                )

                NavigationBarItem(

                    icon = { Icon(Icons.Filled.List, contentDescription = "Screen One") },

                    label = { Text("Screen One") },

                    selected = selectedItem == 1,

                    onClick = {

                        selectedItem = 1

                        navController.navigate(Screen.ScreenOne.route)

                    }

                )

                NavigationBarItem(

                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Screen Two") },

                    label = { Text("Screen Two") },

                    selected = selectedItem == 2,

                    onClick = {

                        selectedItem = 2

                        navController.navigate(Screen.ScreenTwo.route)

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

                ScreenOne()

            }

            composable(Screen.ScreenTwo.route) {

                ScreenTwo()

            }

        }

    }

}

@Composable
fun HomeScreen() {

    Box(

        modifier = Modifier.fillMaxSize(),

        contentAlignment = androidx.compose.ui.Alignment.Center

    ) {

        Text("Home Screen")

    }

}

@Composable
fun ScreenOne() {

    Box(

        modifier = Modifier.fillMaxSize(),

        contentAlignment = androidx.compose.ui.Alignment.Center

    ) {

        Text("Screen One")

    }

}

@Composable
fun ScreenTwo() {

    Box(

        modifier = Modifier.fillMaxSize(),

        contentAlignment = androidx.compose.ui.Alignment.Center

    ) {

        Text("Screen Two")

    }

}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {

    PracticeTheme {

        SecondActivityScreen()

    }

}