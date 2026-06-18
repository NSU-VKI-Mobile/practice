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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

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

    @Composable
    fun MainActivityScreen() {
        val context = LocalContext.current
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // фон
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // цвет заголовка
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ), modifier = Modifier.fillMaxWidth()
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally, // центрируем по горизонтали
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    val intent = Intent(context, MainInputActivity::class.java)
                    context.startActivity(intent)
                }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Рассчитать") }
                Button(onClick = {
                    val intent = Intent(context, HistoryActivity::class.java)
                    context.startActivity(intent)
                }, modifier = Modifier.padding(vertical = 8.dp)) { Text("История расчётов") }
                Button(onClick = {
                    finishAndRemoveTask()
                }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Закрыть приложение") }
            }
        }
    }
}