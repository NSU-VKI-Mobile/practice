package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PracticeTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen(){
        val viewModel: ColorPickerViewModel = viewModel()
        val uiState by viewModel.uiState.collectAsState()
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box( modifier = Modifier
                    .size(100.dp)
                    .background(uiState.color))
                Text(uiState.hexCode,
                    color = uiState.color,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                )
                Slider(
                    value = uiState.red.toFloat(),
                    onValueChange = { viewModel.onRedChanged(it)},
                    valueRange = 0f..255f,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
                )
                Slider(
                    value = uiState.green.toFloat(),
                    onValueChange = { viewModel.onGreenChanged(it)},
                    valueRange = 0f..255f,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp))
                Slider(
                    value = uiState.blue.toFloat(),
                    onValueChange = { viewModel.onBlueChanged(it)},
                    valueRange = 0f..255f,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
                )

                Button(
                    onClick = { viewModel.generateRandomColor() },
                ) { Text("Случайный цвет",
                    fontSize = 20.sp) }
            }
        }
    }
}