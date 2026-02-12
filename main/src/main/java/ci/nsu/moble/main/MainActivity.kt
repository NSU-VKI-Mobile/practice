package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> //Scaffold определяет, где находятся системные панели
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    //тут вызываем функцию экрана
                    ColorInputScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
//чтобы закоментить выделенные строки - на англ раскладке ctrl+/

//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

fun ColorInputScreen(modifier: Modifier = Modifier) {
    // изменяемое состояние для хранения введенного текста
    var colorName by remember { mutableStateOf("") } //TODO это чё?
    //MutableState - специальный класс, который уведомляет Compose об изменениях??
    //Когда значение внутри MutableState меняется,
    // Compose автоматически перерисовывает связанные компоненты????????????
    //remember - функция, сохраняющая данные между вызовами?????????
    //by - дилигирует реализацию другому объекту(что бы это не значило...)
    //TODO вот бы ещё понимать что это значит

    //если без by (что такое делегат...)
    //val state = remember { mutableStateOf("") }
    //var colorName
    //get() = state.value
    //set(value) { state.value = value }
    //понятней не стлао

    TextField(
        value = colorName, //введённое значение в виде объекта String
        onValueChange = { newText -> colorName = newText }, //функция обработки ввода текста (вызывается каждый раз, когда пользователь что-то вводит в текстовое поле.)
        //так... newText - имя, которое мы дали параметру, когда пользователь что-то вводит в TextField, это значение передается сюда
        //лямбдочка присваивает значение параметра newText переменной colorName
        modifier = modifier.fillMaxWidth().padding(16.dp), //чёто с отступами связанное
        label = { Text(text = "Введите название цвета") } //TODO это почему так...
    )
    //получается...пользователь вводит текст > вызывается onValueChange > обновляется colorName > Compose перерисовывает TextField с новым значением
}

@Preview(showBackground = true)
@Composable
//fun GreetingPreview() {
//    PracticeTheme {
//        Greeting("Android")
//    }
//}

//TODO эээээ?...
//@Preview привязывается к следующей функции как я поняла...
fun ColorInputScreenPreview(){
    PracticeTheme {
        ColorInputScreen()
    }
}