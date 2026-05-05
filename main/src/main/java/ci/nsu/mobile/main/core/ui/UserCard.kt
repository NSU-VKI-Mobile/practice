package ci.nsu.mobile.main.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.model.dto.UserDto

@Composable
fun UserCard(user: UserDto, modifier: Modifier = Modifier) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text("ID: ${user.id}")
            Text("Логин: ${user.login}")
            Text("Почта: ${user.email} мес.")
            Text("Телефон: ${user.phoneNumber}%")
        }
    }
}