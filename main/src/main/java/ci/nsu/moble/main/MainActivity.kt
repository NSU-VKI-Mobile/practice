package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ci.nsu.moble.main.navigation.MainNavigator
import ci.nsu.moble.main.navigation.MainNavigatorImpl
import ci.nsu.moble.main.navigation.Screen
import ci.nsu.moble.main.ui.screens.*

class MainActivity : ComponentActivity() {

    private lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = MainNavigatorImpl(
            componentContext = defaultComponentContext()
        )

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen by navigator.currentScreen.subscribeAsState()

                    // Переменная для отслеживания выбранного пункта в нижнем меню
                    var selectedItem by remember {
                        mutableStateOf(
                            when (currentScreen) {
                                is Screen.Home -> 0
                                is Screen.Info -> 1
                                is Screen.Settings -> 2
                                else -> 0
                            }
                        )
                    }

                    Scaffold(
                        bottomBar = {
                            // Показываем нижнее меню только для главных экранов
                            if (currentScreen !is Screen.Fourth) {
                                NavigationBar {
                                    NavigationBarItem(
                                        icon = { Text("🏠") },
                                        label = { Text("Главная") },
                                        selected = selectedItem == 0,
                                        onClick = {
                                            selectedItem = 0
                                            navigator.goHome()
                                        }
                                    )
                                    NavigationBarItem(
                                        icon = { Text("ℹ️") },
                                        label = { Text("Инфо") },
                                        selected = selectedItem == 1,
                                        onClick = {
                                            selectedItem = 1
                                            navigator.goToInfo()
                                        }
                                    )
                                    NavigationBarItem(
                                        icon = { Text("⚙️") },
                                        label = { Text("Настройки") },
                                        selected = selectedItem == 2,
                                        onClick = {
                                            selectedItem = 2
                                            navigator.goToSettings()
                                        }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            when (currentScreen) {
                                is Screen.Home -> HomeScreen(
                                    onNavigateToFourth = { text ->
                                        navigator.goToFourth(text)
                                    }
                                )
                                is Screen.Info -> ScreenOneContent()
                                is Screen.Settings -> ScreenTwoContent()
                                is Screen.Fourth -> {
                                    FourthScreen(
                                        text = (currentScreen as Screen.Fourth).text,
                                        onBack = {
                                            navigator.goBack()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}