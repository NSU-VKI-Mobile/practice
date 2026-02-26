package ci.nsu.moble.main

import ci.nsu.moble.main.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        ColorSearchScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ColorSearchScreen() {
    var colorName by remember { mutableStateOf("") }

    var buttonColor by remember { mutableStateOf(Color.Gray) }

    val context = LocalContext.current

    val allColors = remember { getAllColors(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = colorName,
            onValueChange = { colorName = it },
            label = { Text("Color name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                val colorResId = context.resources.getIdentifier(
                    colorName.trim().lowercase(),
                    "color",
                    context.packageName
                )

                if (colorResId != 0) {
                    val retrievedColor = ContextCompat.getColor(context, colorResId)
                    buttonColor = Color(retrievedColor)
                    Log.e("ColorSearch", "User color \"$colorName\" found")
                } else {
                    Log.e("ColorSearch", "User color \"$colorName\" didn't find")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply color")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Available Colors:"
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allColors) { (name, resId) ->
                val colorValue = Color(ContextCompat.getColor(context, resId))

                Button(
                    onClick = {
                        buttonColor = colorValue
                        colorName = name
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorValue)
                ) {
                    Text(
                        text = name,
                        color = if (isDarkColor(colorValue)) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

fun isDarkColor(color: Color): Boolean {
    val luminance = (color.red * 0.299f + color.green * 0.587f + color.blue * 0.114f)
    return luminance < 0.5f
}

fun getAllColors(context: Context): List<Pair<String, Int>> {
    val colorList = mutableListOf<Pair<String, Int>>()

    try {
        val colorFields = ci.nsu.moble.main.R.color::class.java.fields

        for (field in colorFields) {
            try {
                val name = field.name

                val isLibraryResource = name.startsWith("abc_") ||
                        name.startsWith("material_") ||
                        name.startsWith("m3_") ||
                        name.startsWith("androidx_") ||
                        name.startsWith("design_") ||
                        name.startsWith("notification_")

                if (!isLibraryResource) {
                    val colorResId = field.getInt(null)
                    colorList.add(name to colorResId)

                }
            } catch (e: Exception) {
                continue
            }
        }
    } catch (e: Exception) {
        Log.e("ColorSearch", "Ошибка доступа к локальным цветам: ${e.message}")
    }

    return colorList
}


@Preview(showBackground = true)
@Composable
fun ColorSearchPreview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}