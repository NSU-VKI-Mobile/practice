package ci.nsu.moble.main

import ShoppingViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShoppingListScreen() {
    val viewModel: ShoppingViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(60.dp)
            .size(800.dp)
    )
    {
        Column()
        {
            TextField(
                value = uiState.newItemText,
                textStyle = TextStyle(fontSize = 25.sp),
                placeholder = { Text("Введите текст") },
                onValueChange = { newText ->
                    viewModel.onNewItemTextChanged(newText)

                }

            )
            Button(
                onClick = {
                    viewModel.addItem()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Magenta
                ),
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 5.dp)
                    .height(40.dp)
                    .fillMaxSize()
            ) {
                Text("Добавить")
            }
        }

        LazyColumn()
        {
            items(uiState.items) { item ->
                ShoppingItemRow(
                    item = item,
                    onCheckedChange = { viewModel.toggleItemBought(item.id) },
                    onDelete = { viewModel.deleteItem(item.id) }
                )
            }
        }
    }

}

@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {

    Row(modifier = Modifier.padding(0.dp, 8.dp))
    {
        Checkbox(
            checked = item.isBought,
            onCheckedChange = { onCheckedChange() },
            colors = CheckboxDefaults.colors(Color.Gray)

        )
        Text(
            text = item.name,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onDelete,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
        )
        {
            Text("Удалить")
        }
    }
}