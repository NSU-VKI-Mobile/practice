package com.example.laba4.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laba4.screens.ui.theme.Laba4Theme
import com.example.laba4.viewModels.ShoppingViewModel
import com.example.laba4.viewModels.ShoppingItem
import com.example.laba4.viewModels.ShoppingListUiState

@Preview
@Composable
fun MainScreen(
    viewModel: ShoppingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        Text("Список покупок",
            fontSize=32.sp,
            textAlign=TextAlign.Center,
            fontWeight= FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp)
        )

        TextField(
            value = uiState.newItemText,
            onValueChange = { viewModel.onNewItemTextChanged(it) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Что купим сегодня?..")}
        )

        Button(onClick = {viewModel.addItem()},
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text("Добавить в список",
                textAlign = TextAlign.Center)
        }

        LazyColumn(
            modifier = Modifier.padding(10.dp)
            .border(2.dp, Color.Gray)) {
            items(
                items=uiState.items,
                key={it.id}
            ) { item -> ShoppingItemRow(
                item = item,
                onToggleBought = { viewModel.toggleItemBought(item.id)},
                onDelete = { viewModel.deleteItem(item.id)}
            ) }
        }
    }
}

// Компонент для отображения одного элемента списка
@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onToggleBought: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: Checkbox + название
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isBought,
                    onCheckedChange = { onToggleBought() }
                )

                Text(
                    text = item.name,
                )
            }

            // Правая часть: кнопка удаления
            Button(onDelete) {
                Text("X")
            }

        }
    }
}