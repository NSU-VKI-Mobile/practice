package ci.nsu.moble.main

import android.app.Activity
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.navigation.compose.composable
import ci.nsu.moble.main.ui.Screens.HomeScreen
import ci.nsu.moble.main.ui.Screens.ScreenOneContent
import ci.nsu.moble.main.ui.Screens.ScreenTwoContent
import androidx.compose.material.icons.filled.*

// TODO: crate sealed class with 3 routes
sealed class Screen(val route: String, val title: String, val icon: ImageVector){
    object Home : Screen("home", title = "Home", icon = Icons.Filled.Home)
    object ScreenOne : Screen("screen_one", title = "Screen One", icon = Icons.Filled.List)
    object ScreenTwo : Screen("screen_two", "Screen Two", icon = Icons.Filled.Settings)
}

val screens = listOf(
    Screen.Home,
    Screen.ScreenOne,
    Screen.ScreenTwo
)

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
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(receivedText) }, navigationIcon = {
                IconButton(onClick = {
                    // TODO: create intent and start MainActivity
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
            screens.forEachIndexed { index, screen ->
                NavigationBarItem(

                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title
                        )
                    },
                    label = {Text(screen.title)},
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }) { innerPadding ->
        // TODO: create a nav graph with 3 screens
        // NavHost() {}
        // composable(Screen.Home.route) { HomeScreen() }
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Home.route){ HomeScreen() }
            composable(Screen.ScreenOne.route){ ScreenOneContent() }
            composable(Screen.ScreenTwo.route){ ScreenTwoContent() }
        }
    }
}
