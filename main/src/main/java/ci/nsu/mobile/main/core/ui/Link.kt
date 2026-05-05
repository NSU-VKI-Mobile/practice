package ci.nsu.mobile.main.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Link(
    text: String,
    onClicked: () -> Unit
) {
    Text(
        text = text ,
        color = Color.LightGray,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { onClicked() }
    )
}