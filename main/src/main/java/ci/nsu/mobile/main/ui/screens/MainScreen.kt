package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ci.nsu.mobile.main.R

@Composable
fun MainScreen(
    onExitClick: () -> Unit
){
    val spacer = ""
    val boolSpacer = true
    Column() {
        Text("123")
        Button(onClick = onExitClick) {
            Text(stringResource(R.string.text_exit))
        }
    }
}