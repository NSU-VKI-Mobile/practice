package ci.nsu.mobile.main

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {

    val navController = rememberNavController()

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

                val intent = Intent(
                    context,
                    SecondActivity::class.java
                )

                intent.putExtra(
                    "message",
                    text
                )

                context.startActivity(intent)
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