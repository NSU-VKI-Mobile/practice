package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ShoppingScreen()
            }
        }
    }
}

@Composable
fun ShoppingScreen() {

    var items by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                label = { Text("Товар") }
            )

            Button(
                onClick = {
                    val newText = text.trim()
                    if (newText.isNotBlank() &&
                        items.none { it.name.equals(newText, ignoreCase = true) }
                    ) {
                        items = items + ShoppingItem(
                            id = items.size + 1,
                            name = newText
                        )
                        text = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(items) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = item.isBought,
                        onCheckedChange = {
                            items = items.map {
                                if (it.id == item.id) it.copy(isBought = !it.isBought)
                                else it
                            }
                        }
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        textDecoration = if (item.isBought)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                    )

                    Button(
                        onClick = {
                            items = items.filter { it.id != item.id }
                        }
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}