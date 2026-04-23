package ci.nsu.mobile.main.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun PracticeTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography(),
        content = content
    )
}