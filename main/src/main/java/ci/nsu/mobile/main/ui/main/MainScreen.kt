package ci.nsu.mobile.main.ui.main

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ci.nsu.mobile.main.ui.base.BaseScreen
import ci.nsu.mobile.main.ui.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onCalculate: () -> Unit, onHistory: () -> Unit, onExit: () -> Unit, modifier: Modifier = Modifier) {
    BaseScreen(title = "Главная") {
        Button(
            onClick = { onCalculate() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Расчитать")
        }
        Button(onClick = { onHistory() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История")
        }
        Button(
            onClick = { onExit() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выход")
        }
    }
}