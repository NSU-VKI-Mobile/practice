package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

sealed class SecondScreen(
    val route: String,
    val title: String
) {
    object Home : SecondScreen("home", "Home")
    object ScreenOne : SecondScreen("screen_one", "Screen One")
    object ScreenTwo : SecondScreen("screen_two", "Screen Two")
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
    val context = LocalContext.current
    val navController = rememberNavController()

    val receivedText = remember {
        if (context is Activity) {
            context.intent.getStringExtra("message") ?: "No text received"
        } else {
            "No text received"
        }
    }

    val bottomItems = listOf(
        SecondScreen.Home,
        SecondScreen.ScreenOne,
        SecondScreen.ScreenTwo
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = {
                    Text(receivedText)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (context is Activity) {
                                context.finish()
                            }
                        }
                    ) {
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

                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            when (item) {
                                SecondScreen.Home -> {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Home"
                                    )
                                }

                                SecondScreen.ScreenOne -> {
                                    Icon(
                                        imageVector = Icons.Filled.List,
                                        contentDescription = "Screen One"
                                    )
                                }

                                SecondScreen.ScreenTwo -> {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Screen Two"
                                    )
                                }
                            }
                        },
                        label = {
                            Text(item.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SecondScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SecondScreen.Home.route) {
                SecondHomeScreen(receivedText)
            }

            composable(SecondScreen.ScreenOne.route) {
                ScreenOne()
            }

            composable(SecondScreen.ScreenTwo.route) {
                ScreenTwo()
            }
        }
    }
}

@Composable
fun SecondHomeScreen(receivedText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SecondActivity")
        Text("Полученный текст:")
        Text(receivedText)
    }
}

@Composable
fun ScreenOne() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Screen One")
        Text("Это первый экран нижнего меню")
    }
}

@Composable
fun ScreenTwo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Screen Two")
        Text("Это второй экран нижнего меню")
    }
}

@Preview(showBackground = true)
@Composable
fun SecondActivityPreview() {
    PracticeTheme {
        SecondHomeScreen("Preview text")
    }
}