package ci.nsu.moble.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ci.nsu.moble.main.ui.main.MainFragment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment

class MainActivity : AppCompatActivity() {

    val colorsMap = mapOf(
        "red" to Color.Red,
        "blue" to Color.Blue,
        "green" to Color.Green,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
        "crimson" to Color(220, 20, 60),
        "turqoise" to Color(64, 224, 208)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    @Composable
    fun UserTheme() {
        var inputText by remember { mutableStateOf("") }
        var buttonColor by remember { mutableStateOf(Color.Green) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Введите название цвета:") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val key = inputText.trim().lowercase()
                    val foundColor = colorsMap[key]

                    if (foundColor != null) {
                        buttonColor = foundColor
                    } else {
                        Log.d("UserTheme", "Цвет \"$inputText\" не найден")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Применить")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Палитра", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(colorsMap.entries.toList()) { entry ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(vertical = 4.dp)
                            .background(entry.value),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.key,
                            color = if (entry.value == Color.Black) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}


