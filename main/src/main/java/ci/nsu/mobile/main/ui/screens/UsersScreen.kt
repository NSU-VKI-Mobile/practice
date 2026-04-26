package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.ui.components.CustomButton

@Composable
fun UsersScreen(navTo: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        CustomButton({navTo(Screens.LoginScreen.route)}, "Выйти")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUsers() {
    UsersScreen({})
}