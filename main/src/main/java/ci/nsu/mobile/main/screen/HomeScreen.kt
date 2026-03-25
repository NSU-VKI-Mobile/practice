package ci.nsu.mobile.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.ui.theme.PracticeTheme

@Composable
fun HomeScreen(
    onCalculateClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onExitClick: () -> Unit = {}
) {
    PracticeTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // пупупу
        }
    }
}