package ci.nsu.mobile.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
// navController управляет переходами между экранами нижнего меню
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {}
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }

            composable(BottomNavItem.Profile.route) {
                SimpleScreen(
                    title = "Профиль",
                    text = "Это экран профиля"
                )
            }

            composable(BottomNavItem.Settings.route) {
                SimpleScreen(
                    title = "Настройки",
                    text = "Это экран настроек"
                )
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

// Переменная хранит текст, который пользователь ввёл в поле
    var text by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Главный экран",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = {
                Text("Введите текст")
            },
            modifier = Modifier.widthIn(max = 320.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
// Создаём Intent для перехода во вторую Activity
                val intent = Intent(context, SecondActivity::class.java)

// Передаём введённый текст во вторую Activity
                intent.putExtra("message", text)

// Запускаем вторую Activity
                context.startActivity(intent)
            }
        ) {
            Text("Открыть второй экран")
        }
    }
}

@Composable
fun SimpleScreen(
    title: String,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = text)
        }
    }
}