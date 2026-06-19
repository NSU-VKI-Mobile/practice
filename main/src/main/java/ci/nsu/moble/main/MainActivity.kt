package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                MainActivityScreen()
            }
        }
    }
}

@Composable
fun MainActivityScreen() {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Main Activity",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text to send") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("text_data", text)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open SecondActivity")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    PracticeTheme {
        MainActivityScreen()
    }
}