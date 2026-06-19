package ci.nsu.moble.main

import android.app.Activity
import android.content.Intent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

    // Получаем текст из Intent
    val receivedText = remember {
        if (context is Activity) {
            context.intent.getStringExtra("text_data") ?: "No text received"
        } else {
            "No text received"
        }
    }

    // Глобальное состояние для текста, который будет передан в Screen One
    var sharedText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        selectedItem = 0
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Screen One") },
                    label = { Text("Screen One") },
                    selected = selectedItem == 1,
                    onClick = {
                        navController.navigate(Screen.ScreenOne.route)
                        selectedItem = 1
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Screen Two") },
                    label = { Text("Screen Two") },
                    selected = selectedItem == 2,
                    onClick = {
                        navController.navigate(Screen.ScreenTwo.route)
                        selectedItem = 2
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToScreenTwo = {
                        navController.navigate(Screen.ScreenTwo.route)
                        selectedItem = 2
                    }
                )
            }

            composable(Screen.ScreenOne.route) {
                ScreenOneContent(
                    textFromScreenTwo = sharedText
                )
            }

            composable(Screen.ScreenTwo.route) {
                ScreenTwoContent(
                    onNavigateToScreenOne = { text ->
                        sharedText = text
                        navController.navigate(Screen.ScreenOne.route)
                        selectedItem = 1
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigateToScreenTwo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Welcome to the Home Screen!",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onNavigateToScreenTwo,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Go to Screen Two")
        }
    }
}

@Composable
fun ScreenOneContent(textFromScreenTwo: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen One",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "This is Screen One",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Отображение текста из Screen Two
        if (textFromScreenTwo.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Text from Screen Two:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = textFromScreenTwo,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Blue
                    )
                }
            }
        } else {
            Text(
                text = "No text received from Screen Two yet",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ScreenTwoContent(
    onNavigateToScreenOne: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Screen Two",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )

        Text(
            text = "Enter text to send to Screen One:",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Поле для ввода текста
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text here") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Кнопка для отправки текста в Screen One и перехода
        Button(
            onClick = {
                if (inputText.isNotEmpty()) {
                    onNavigateToScreenOne(inputText)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Send to Screen One")
        }

        // Информация о текущем введенном тексте
        if (inputText.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current text:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = inputText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecondActivityPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}