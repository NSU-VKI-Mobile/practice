package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent // ДОБАВЛЕНО: нужен для возврата в MainActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box // ДОБАВЛЕНО: для простых экранов внутри NavHost
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // ДОБАВЛЕНО: для innerPadding из Scaffold
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
import androidx.compose.runtime.getValue // ДОБАВЛЕНО: нужно для currentBackStackEntryAsState()
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment // ДОБАВЛЕНО: для центрирования текста на экранах
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination // ДОБАВЛЕНО: для корректной навигации bottom menu
import androidx.navigation.compose.NavHost // ДОБАВЛЕНО: навигационный контейнер
import androidx.navigation.compose.composable // ДОБАВЛЕНО: описание экранов
import androidx.navigation.compose.currentBackStackEntryAsState // ДОБАВЛЕНО: чтобы понимать текущий экран
import androidx.navigation.compose.rememberNavController // ДОБАВЛЕНО: navController
import ci.nsu.moble.main.ui.theme.PracticeTheme

// ДОБАВЛЕНО: sealed class с тремя маршрутами для экранов нижнего меню
sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object ScreenOne : Screen("screen_one", "Screen One")
    data object ScreenTwo : Screen("screen_two", "Screen Two")
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
    // ДОБАВЛЕНО: создаем NavController для переходов между экранами нижнего меню
    val navController = rememberNavController()

    val context = LocalContext.current

    // ИЗМЕНЕНО: вместо mutableStateOf можно сразу получить строку из intent
    // Так проще, потому что текст просто читается один раз
    val receivedText = if (context is Activity) {
        context.intent.getStringExtra("text_data") ?: "No text received"
    } else {
        "No text received"
    }

    // ДОБАВЛЕНО: список экранов для нижней навигации
    val screens = listOf(Screen.Home, Screen.ScreenOne, Screen.ScreenTwo)

    // ДОБАВЛЕНО: определяем текущий маршрут, чтобы подсвечивать выбранный пункт меню
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                // Уже было: теперь здесь отображается переданный текст
                title = { Text(receivedText) },
                navigationIcon = {
                    IconButton(onClick = {
                        // ДОБАВЛЕНО: переход обратно в MainActivity из TopBar
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)

                        // Было: finish() оставляем, чтобы закрыть текущее Activity
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
                // ИЗМЕНЕНО: вместо selectedItem используется currentRoute и список screens
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> Icons.Filled.Home
                                    Screen.ScreenOne -> Icons.Filled.List
                                    Screen.ScreenTwo -> Icons.Filled.Settings
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route, // ДОБАВЛЕНО: активный экран определяется через route
                        onClick = {
                            // ДОБАВЛЕНО: переход между экранами через navController
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // ДОБАВЛЕНО: граф навигации с 3 экранами
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

// ДОБАВЛЕНО: экран Home
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Это Home Screen")
    }
}

// ДОБАВЛЕНО: экран Screen One
@Composable
fun ScreenOneContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Это Screen One")
    }
}

// ДОБАВЛЕНО: экран Screen Two
@Composable
fun ScreenTwoContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Это Screen Two")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}