@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import kotlin.jvm.java

class MainInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PracticeTheme {
                MainInputActivityScreen()
            }
        }


    }
    @Composable
    fun EditTextComposable(
        labelText: String,
        value: String,
        onValueChange: (String) -> Unit
    ){
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(labelText) }
        )
    }

    @Composable
    fun MainInputActivityScreen(){
        val context = LocalContext.current
        var startAmount by remember { mutableStateOf("") }
        var termMonths by remember { mutableStateOf("") }

        Scaffold(modifier = Modifier.fillMaxSize(), topBar =
            {
                TopAppBar(
                    title = { Text("Расчёт вкладов") },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // фон
                        titleContentColor = MaterialTheme.colorScheme.onPrimary, // цвет заголовка
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally, // центрируем по горизонтали
                verticalArrangement = Arrangement.Center) {

                EditTextComposable(
                    "Стартовый взнос",
                    startAmount,
                    { if (it.isDigitsOnly()) startAmount = it}
                )
                EditTextComposable(
                    "Срок вклада в месяцах",
                    termMonths,
                    { if (it.isDigitsOnly()) termMonths = it}
                )

                Button(onClick = {
                    val startAmountValue = startAmount.toDoubleOrNull() ?: 0.0
                    val termMonthsValue = termMonths.toIntOrNull() ?: 0
                    val intent = Intent(context, SecondInputActivity::class.java).apply {
                        putExtra("START_AMOUNT", startAmountValue)
                        putExtra("TERM", termMonthsValue)
                    }
                    context.startActivity(intent)
                }) { Text("Далее") }
            }
        }
    }

    }
