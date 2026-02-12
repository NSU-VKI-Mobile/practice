package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

// TODO: crate sealed class with 3 routes
enum class LunchTrayScreen(@StringRes val title: Int) {
    Home(R.string.home),
    First(R.string.first_screen),
    Second(R.string.second_screen)
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreen.First.name
    )

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(receivedText) }, navigationIcon = {
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
                    selectedItem = 0
                    navController.navigate(LunchTrayScreen.Home.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                label = { Text("Screen One") },
                selected = selectedItem == 1,

                onClick = {
                    selectedItem = 1
                    navController.navigate(LunchTrayScreen.First.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                label = { Text("Screen Two") },
                selected = selectedItem == 2,
                onClick = {
                    selectedItem = 2
                    navController.navigate(LunchTrayScreen.Second.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                })
        }
    }) { innerPadding ->
        // TODO: create a nav graph with 3 screens
        NavHost(navController = navController,
            startDestination = LunchTrayScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LunchTrayScreen.Home.name) {
                Box(Modifier.fillMaxSize()) {
                    Text("Home Screen", modifier = Modifier.align(Alignment.Center))
                }
            }
            composable(LunchTrayScreen.First.name) {
                Box(Modifier.fillMaxSize()) {
                    Text("First Screen", modifier = Modifier.align(Alignment.Center))
                }
            }
            composable(LunchTrayScreen.Second.name) {
                Box(Modifier.fillMaxSize()) {
                    Text("Second Screen", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    PracticeTheme {
//        SecondActivityScreen()
//    }
//}