package ci.nsu.moble.main.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

@Composable
fun ScreenTwoContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("This is Screen Two")
    }
}
@Preview(showBackground = true)
@Composable
fun ScreenTwoContentPreview() {
    PracticeTheme {
        ScreenTwoContent()
    }
}