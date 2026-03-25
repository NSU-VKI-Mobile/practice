package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.compose.ui.graphics.Color as c

class MainActivity : ComponentActivity() {
    private val viewModel: ShoppingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {

                MyScreen(viewModel = viewModel)
            }
        }
    }
}


@Composable
fun MyScreen(
    viewModel: ShoppingViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement=Arrangement.Top,
        horizontalAlignment=Alignment.CenterHorizontally
    ){
        TextField(
            value = uiState.newItemText,
            onValueChange = {newText -> viewModel.onNewItemTextChanged(newText)},
            label = {Text("")},
            modifier= Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {viewModel.addItem(); println(uiState.items)
                },
            modifier = Modifier
                .height(height = 48.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006400)
            )
            ){
            Text(text = "Добавить")
        }
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn {
            items(uiState.items, key = {item -> item.id}){
                item ->
                Surface (
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFDCDCDC),
                    modifier = Modifier.padding(4.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(modifier = Modifier
                            .fillMaxSize())
                        {
                            Text(
                                text = item.name,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    viewModel.deleteItem(item.id)
                                },
                                modifier = Modifier
                                    .height(height = 36.dp)
                                    .width(128.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF8B0000)
                                )
                            ) {
                                Text(text = "Удалить")
                            }
                        }
                        Checkbox(
                            checked = item.isBought,
                            onCheckedChange = {viewModel.toggleItemBought(item.id)}
                        )

                    }
                }
            }
        }
    }
}
