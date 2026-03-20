package com.example.sheydaevtusk3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLunchTrayApp()
        }
    }
}

@Composable
fun SimpleLunchTrayApp() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf("Start") }

    Scaffold(
        topBar = {
            Row {
                if (navController.previousBackStackEntry != null) {
                    Button(onClick = { navController.navigateUp() }) {
                        Text("← Назад")
                    }
                }
                Text("  Текущий экран: $currentScreen")
            }
        },
        bottomBar = {
            Row {
                Button(onClick = {
                    navController.navigate("Start")
                    currentScreen = "Start"
                }) { Text("🏠 Главная") }

                Button(onClick = {
                    navController.navigate("Entree")
                    currentScreen = "Entree"
                }) { Text("🍔 Меню") }

                Button(onClick = {
                    navController.navigate("Checkout")
                    currentScreen = "Checkout"
                }) { Text("🛒 Корзина") }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "Start",
            modifier = Modifier.padding(padding)
        ) {
            composable("Start") {
                StartScreen(
                    onNavigateToEntree = {
                        navController.navigate("Entree")
                        currentScreen = "Entree"
                    }
                )
            }

            composable("Entree") {
                EntreeScreen(
                    onNext = {
                        navController.navigate("SideDish")
                        currentScreen = "SideDish"
                    },
                    onCancel = {
                        navController.popBackStack("Start", false)
                        currentScreen = "Start"
                    }
                )
            }

            composable("SideDish") {
                SideDishScreen(
                    onNext = {
                        navController.navigate("Accompaniment")
                        currentScreen = "Accompaniment"
                    },
                    onCancel = {
                        navController.popBackStack("Start", false)
                        currentScreen = "Start"
                    }
                )
            }

            composable("Accompaniment") {
                AccompanimentScreen(
                    onNext = {
                        navController.navigate("Checkout")
                        currentScreen = "Checkout"
                    },
                    onCancel = {
                        navController.popBackStack("Start", false)
                        currentScreen = "Start"
                    }
                )
            }

            composable("Checkout") {
                CheckoutScreen(
                    onSubmit = {
                        navController.popBackStack("Start", false)
                        currentScreen = "Start"
                    },
                    onCancel = {
                        navController.popBackStack("Start", false)
                        currentScreen = "Start"
                    }
                )
            }
        }
    }
}

@Composable
fun StartScreen(onNavigateToEntree: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Добро пожаловать в Lunch Tray!")

        Button(onClick = onNavigateToEntree) {
            Text("Начать заказ (NavHost)")
        }

        Button(onClick = {
            context.startActivity(Intent(context, SecondActivity::class.java).apply {
                putExtra("key_data", "Привет из MainActivity! Это Task 3")
            })
        }) {
            Text("Переход по Intent")
        }
    }
}

@Composable
fun EntreeScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column {
        Text("Выберите основное блюдо")
        Text("🍔 Бургер - 8.99$")
        Text("🍕 Пицца - 12.99$")
        Text("🥗 Салат - 7.99$")

        Row {
            Button(onClick = onCancel) { Text("Отмена") }
            Button(onClick = onNext) { Text("Далее →") }
        }
    }
}

@Composable
fun SideDishScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column {
        Text("Выберите гарнир")
        Text("🍟 Картошка фри - 3.99$")
        Text("🍚 Рис - 2.99$")
        Text("🥦 Овощи - 4.99$")

        Row {
            Button(onClick = onCancel) { Text("Отмена") }
            Button(onClick = onNext) { Text("Далее →") }
        }
    }
}

@Composable
fun AccompanimentScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column {
        Text("Выберите дополнение")
        Text("🧀 Сыр - 1.99$")
        Text("🥑 Авокадо - 2.49$")
        Text("🥓 Бекон - 2.99$")

        Row {
            Button(onClick = onCancel) { Text("Отмена") }
            Button(onClick = onNext) { Text("Далее →") }
        }
    }
}

@Composable
fun CheckoutScreen(onSubmit: () -> Unit, onCancel: () -> Unit) {
    Column {
        Text("🛒 ВАШ ЗАКАЗ:")
        Text("✅ Бургер - 8.99$")
        Text("✅ Картошка фри - 3.99$")
        Text("✅ Сыр - 1.99$")
        Text("──────────────")
        Text("💰 ИТОГО: 14.97$")

        Row {
            Button(onClick = onCancel) { Text("Отмена") }
            Button(onClick = onSubmit) { Text("✅ Заказать") }
        }
    }
}