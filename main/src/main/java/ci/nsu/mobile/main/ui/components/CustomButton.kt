package ci.nsu.mobile.main.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomButton(onClick: () -> Unit, title: String) {
    Button(onClick = onClick) {
        Text(title)
    }
}