package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.ui.ShoppingItem
import ci.nsu.mobile.main.ui.ShoppingViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme


private val Int.id: Any

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun ShoppingScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel = ShoppingViewModel()
) {
    val uiState by shoppingViewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Секция для добавления нового товара
        AddItemSection(
            newItemText = uiState.newItemText,
            onTextChanged = shoppingViewModel::onNewItemTextChanged,
            onAddItem = shoppingViewModel::addItem
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Список товаров
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.items, key = { it.id }) { item ->
                ShoppingListItem(
                    item = item,
                    onToggleBought = { shoppingViewModel.toggleItemBought(item.id) },
                    onDelete = { shoppingViewModel.deleteItem(item.id) }
                )
            }
        }
    }
}

@Composable
fun AddItemSection(
    newItemText: String,
    onTextChanged: (String) -> Unit,
    onAddItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = newItemText,
            onValueChange = onTextChanged,
            label = { Text("Введите покупку") },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onAddItem, enabled = newItemText.isNotBlank()) {
            Text("Добавить")
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onToggleBought: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isBought,
                onCheckedChange = { onToggleBought() }
            )
            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить товар"
                )
            }
        }
    }
}