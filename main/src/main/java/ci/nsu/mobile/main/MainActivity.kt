package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Главная") },
                selected = navController.currentDestination?.route == State.Main.route,
                onClick = {
                    navController.navigate(State.Main.route) {
                        // Очищаем стек до этого пункта, чтобы не копить экраны
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Дополнительная") },
                selected = navController.currentDestination?.route == State.Second.route,
                onClick = {
                    navController.navigate(State.Second.route) {
                        // Очищаем стек до этого пункта, чтобы не копить экраны
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = State.Main.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(State.Main.route) {
                HomeScreen(innerPadding)
            }
            composable(State.Second.route) {
                SecondScreen("page2")
            }
        }
    }
}

@Composable
fun HomeScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },  // Обновляем состояние при вводе
            label = { Text("Введите текст") },
        )
        Button(
            onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("EXTRA_MESSAGE", inputText)
                context.startActivity(intent)
            }, modifier = Modifier.padding(innerPadding)
        ) {
            Text("Перейти", modifier = Modifier.padding(innerPadding))
        }
    }
}