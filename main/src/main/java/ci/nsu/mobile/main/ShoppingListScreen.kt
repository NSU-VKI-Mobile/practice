package ci.nsu.mobile.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isBought,
            onCheckedChange = { onToggle(item.id) }
        )

        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )

        Button(onClick = { onDelete(item.id) })
        {
            Text("Удалить")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen() {

    val viewModel: ShoppingViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список покупок") })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.newItemText,
                    onValueChange = viewModel::onNewItemTextChanged,
                    label = { Text("Название товара") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )

                Button(
                    onClick = viewModel::addItem,
                    enabled = uiState.newItemText.isNotBlank()
                ) {
                    Text("Добавить")
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    ShoppingListItem(
                        item = item,
                        onToggle = viewModel::toggleItemBought,
                        onDelete = viewModel::deleteItem
                    )
                }
            }

            if (uiState.items.isEmpty()) {
                Text(
                    text = "Список пока пуст. Добавьте первый товар!",
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}