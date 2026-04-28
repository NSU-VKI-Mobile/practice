package ci.nsu.moble.main.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

@Composable
fun ScreenOneContent() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Yellow // Укажите нужный цвет
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("This is Screen One")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ScreenOneContentPreview() {
    PracticeTheme {
        ScreenOneContent()
    }
}