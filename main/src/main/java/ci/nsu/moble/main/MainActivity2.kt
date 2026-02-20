package ci.nsu.moble.main
import android.util.Log
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box

import androidx.compose.material3.Button

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // елает полный экран? чето както не знаю зачем, но пусть будет
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
    val colors = mapOf( // ключ значение для переменноц вал неизменяемой. т.е. чтобы при написании строк, мы могли найти элемент
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "magenta" to Color.Magenta
    )
    @Composable
    fun ColorScreen(modifier: Modifier = Modifier) {


        var userInput by remember { mutableStateOf("") } // переменная изменяемая для ввода строк, делегируетчя для compose, запоминается, по умолчанию пустая, ибо пока юзер не введет что либо, должна быть пустй

        var buttonColor by remember {mutableStateOf(Color.Gray)} // та же история но для кнопки

        Column(modifier.padding(16.dp)) { // контейнер который сложит все эелменты вертикально, дополнительно отступ чтоб по красоте
            TextField(
                value = userInput, // берем переменную для ввода
                onValueChange = {userInput = it}, // берем функцию которая будет обновлять каждый раз как юзер чето вводит обновлять переменную этим текст
            )

            Button(
                onClick = { // если кликаем
                    val color = colors[userInput.lowercase()]
                    // берем переменную цветов,
                    if (color != null)
                    {
                        buttonColor = color
                    }
                    else
                    {
                        Log.d("ColorScreen", "Не найден $userInput")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            )
            {Text("Click")}

            colors.forEach { (name, color) ->
                Row {
                    Box(
                        modifier = Modifier.size(32.dp).background(color)
                    )

                }

            }

        }


    }

}