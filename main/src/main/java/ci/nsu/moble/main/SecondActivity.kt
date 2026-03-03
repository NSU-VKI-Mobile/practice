package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

// TODO: crate sealed class with 3 routes
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
    // todo: create nav controller
    val navController = rememberNavController()//управляет переходами
    //var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }//получает текст из mainActivity
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route//определяет на каком экране находимся

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(//верхняяя панель
            title = { Text(receivedText) }, navigationIcon = {
                IconButton(onClick = {
                    // TODO: create intent and start MainActivity
                    if (context is Activity) {
                        context.finish()// закрывает текущую активность и возвращает на предыдущий экран
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
                    // TODO: navigate to home screen by navController
                    navController.navigate(Screen.Home.route) {//осуществляет переход на выбранный экран
                        popUpTo(Screen.Home.route) {//управляет историей навигации(чтобы не создавать копии экранов)
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                label = { Text("Screen One") },
                selected = currentRoute == Screen.ScreenOne.route,

                onClick = {
                    // TODO: navigate to screen one
                    navController.navigate(Screen.ScreenOne.route) {
                        launchSingleTop = true
                    }
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                label = { Text("Screen Two") },
                selected = currentRoute == Screen.ScreenTwo.route,
                onClick = {
                    // TODO: navigate to screen two
                    navController.navigate(Screen.ScreenTwo.route) {
                        launchSingleTop = true
                    }
                })
        }
    }) { innerPadding ->
        // TODO: create a nav graph with 3 screens
        NavHost(//навигация
            navController = navController,
            startDestination = Screen.Home.route,//первый экран
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {//связывает маршрут с функцией отрисовки
                HomeScreen(
                    onNavigateToScreenOne = {
                        navController.navigate(Screen.ScreenOne.route)
                    }
                )
            }
            composable(Screen.ScreenOne.route) {
                ScreenOneScreen(
                    onNavigateToScreenTwo = {
                        navController.navigate(Screen.ScreenTwo.route)
                    }
                )
            }
            composable(Screen.ScreenTwo.route) {
                ScreenTwoScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
@Composable
fun HomeScreen(onNavigateToScreenOne: () -> Unit) {     //каждый экран получает функцию для перехода на следующий экран
                                                        // при нажатии кнопки вызывается эта функция
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home Screen", modifier = Modifier.padding(16.dp))
        androidx.compose.material3.Button(
            onClick = onNavigateToScreenOne,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go to Screen One")
        }
    }
}
@Composable
fun ScreenOneScreen(onNavigateToScreenTwo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen One", modifier = Modifier.padding(16.dp))
        androidx.compose.material3.Button(
            onClick = onNavigateToScreenTwo,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go to Screen Two")
        }
    }
}
@Composable
fun ScreenTwoScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen Two", modifier = Modifier.padding(16.dp))
        androidx.compose.material3.Button(
            onClick = onNavigateBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go Back")
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