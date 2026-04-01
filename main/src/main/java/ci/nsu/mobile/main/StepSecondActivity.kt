package ci.nsu.mobile.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.DpOffset
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class StepSecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StepSecondScreenActivity(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepSecondScreenActivity(modifier: Modifier = Modifier
    .background(Color.LightGray))
{
    val context = LocalContext.current
    val text = remember { mutableStateOf("") }
    val text_list = remember {mutableStateOf("")}
    val list = listOf("Рубли", "Доллары", "Евро")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Расчет вкладов") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {

            Text(text="Валюта *")

            Box(
                modifier = Modifier
            ) {
                Row {
                    TextField(
                        value = selectedOption,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        placeholder = { Text("Выберите валюту") },
                        onValueChange = { newText ->
                            selectedOption = newText
                        },
                        readOnly = true
                    )

                    IconButton(onClick = {expanded = true}) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Показать валюты")
                    }

                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {expanded=false},
                    offset = DpOffset(x=220.dp, y=0.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {selectedOption = "Рубли"},
                        text = {Text("Рубли")}
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        onClick = {selectedOption = "Доллары"},
                        text = {Text("Доллары")}
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        onClick = {selectedOption = "Евро"},
                        text = {Text("Евро")}
                    )
                }

            }


            Spacer(modifier = Modifier
                .padding(10.dp))

            Text(text="Сумма ежемесячного пополнения")

            TextField(
                value = text.value,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 25.sp),
                placeholder = { Text("Введите сумму") },
                onValueChange = { newText ->
                    text.value = newText
                })

            Row(modifier = Modifier
                .height(100.dp)
            ){
                Button(
                    onClick = {
                        if (context is Activity) {
                            context.finishAffinity()
                            val intent = Intent(context, StepFirstActivity::class.java)
                            context.startActivity(intent)
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(170.dp)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        val intent = Intent(context, ResultActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(170.dp)

                ) {
                    Text("Рассчитать")
                }


            }

        }

    }

}



@Preview(showBackground = true)
@Composable
fun StepSecondPreview() {
    PracticeTheme {
        StepSecondScreenActivity()
    }
}