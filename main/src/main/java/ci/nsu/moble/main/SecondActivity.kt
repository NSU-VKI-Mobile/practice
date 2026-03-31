package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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
import ci.nsu.moble.main.ui.theme.PracticeTheme

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.sp


// TODO: crate sealed class with 3 routes

sealed class Screens(val route: String, val title: String) {
    object Home : Screens(route= "Home", title= "Home")
    object First : Screens(route= "First", title= "Screen One")
    object Second : Screens(route= "Second",title= "Screen Two")
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
    // todo: create nav controller
    var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }

    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(receivedText) }, navigationIcon = {
                IconButton(onClick = {
                    // TODO: create intent and start MainActivity

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
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue, titleContentColor = Color.White
            )
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = selectedItem == 0,

                onClick = {
                    // TODO: navigate to home screen by navController
                    selectedItem = 0
                    navController.navigate(Screens.Home.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                label = { Text("Screen One") },
                selected = selectedItem == 1,

                onClick = {
                    // TODO: navigate to screen one
                    selectedItem = 1
                    navController.navigate(Screens.First.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                label = { Text("Screen Two") },
                selected = selectedItem == 2,
                onClick = {
                    // TODO: navigate to screen two
                    selectedItem = 2
                    navController.navigate(Screens.Second   .route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                })
            }
        })
        { innerPadding ->
            // TODO: create a nav graph with 3 screens
            NavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier.padding(innerPadding)
            )
            {
                composable(Screens.Home.route) {
                    Text("This is Home screen", fontSize = 30.sp)
                }
                composable(Screens.First.route) {
                    Text("This is Screen One", fontSize = 30.sp)
                }
                composable(Screens.Second.route) {
                    Text("This is Screen Two", fontSize = 30.sp)
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