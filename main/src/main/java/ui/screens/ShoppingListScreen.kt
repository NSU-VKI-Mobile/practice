package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodels.MainViewModel
import kotlinx.coroutines.launch

private fun isDuplicate(items: List<String>, newItem: String): Boolean { //ИBoolean возвращает true если есть дубликат
    val trimmedNew = newItem.trim() // trim удаляет пробел
    return items.any { existing ->
        existing.trim().equals(trimmedNew, ignoreCase = true) //equals игнорит регистр
    }
}

@Composable
fun ShoppingListScreen(
    navController: NavController,
    date: String,
    viewModel: MainViewModel
) {
    var items by remember { mutableStateOf<List<String>>(emptyList()) }
    var newItem by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(date) { //загрузка данных при открытии
        isLoading = true
        items = viewModel.getListForDate(date)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Добавить заметку на $date",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row( //строка с добавлением
            modifier = Modifier.fillMaxWidth(), //на всю ширину
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField( //поле ввода
                value = newItem,
                onValueChange = { newItem = it }, //при каждом обновлении newItem = теькст
                label = { Text("Новый пункт") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (newItem.isNotBlank()) {
                        if (isDuplicate(items, newItem)) {
                            android.widget.Toast.makeText( // сообщение, что такой пункт уже есть
                                navController.context,
                                "Такой пункт уже есть в списке!",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            items = items + newItem.trim()
                            newItem = ""
                        }
                    }
                }
            ) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) { //закрузка данных с бд
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { //контейнер во весь экран
                CircularProgressIndicator() //крутилка
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f), //занимает все место
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Card( //пункты списка
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween //текст влево, кнопка вправо
                        ) {
                            Text(text = item)
                            IconButton(onClick = {
                                items = items - item
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {                                                                                                                                                                                              //ассинхрон
                        if (items.isNotEmpty()) {
                            viewModel.saveList(date, items)
                        } else {
                            viewModel.deleteList(date)
                        }
                        navController.popBackStack() //возращение на календарь
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }

            Button(
                onClick = {
                    scope.launch {
                        viewModel.deleteList(date)
                        navController.popBackStack() //возращение на календарь
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Удалить всё")
            }
        }
    }
}