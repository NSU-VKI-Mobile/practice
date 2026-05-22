package ci.nsu.mobile.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
fun MainScreen() {

    val navController = rememberNavController()//С помощью него происходит переключение экранов нижнего меню.

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(

        bottomBar = {

            NavigationBar {

                items.forEach { item ->

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(item.title)
                        },
                        icon = {}
                    )
                }
            }
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }

            composable(BottomNavItem.Profile.route) {
                ScreenContent("Profile Screen")
            }

            composable(BottomNavItem.Settings.route) {
                ScreenContent("Settings Screen")
            }
        }
    }
}

@Composable
fun HomeScreen() {

    val context = LocalContext.current

    var text by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Home Screen")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = {
                Text("Введите текст")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val intent = Intent(context,SecondActivity::class.java)

                intent.putExtra(
                    "message",//ключ
                    text//введеная пользователем строка
                )

                context.startActivity(intent)//открывает SecondActivity.
            }
        ) {

            Text("Открыть SecondActivity")
        }
    }
}

@Composable
fun ScreenContent(text: String) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(text)
    }
}