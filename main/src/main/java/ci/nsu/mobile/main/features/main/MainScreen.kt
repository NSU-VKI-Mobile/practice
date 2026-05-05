package ci.nsu.mobile.main.features.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.core.ui.BaseScreen
import ci.nsu.mobile.main.core.ui.ButtonsPanel
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.core.ui.Link
import ci.nsu.mobile.main.core.ui.UserCard

import ci.nsu.mobile.main.features.auth.login.PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainState,
    onRefreshButtonClick: () -> Unit,
    onExitButtonClick: () -> Unit,
    onLogoutButtonClick: () -> Unit,
) {
    BaseScreen(
        title = "Пользователи",
        bottomBar = {
            Column(
                modifier = Modifier
                    .imePadding()
                    .padding(PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PADDING)
            ) {

                ButtonsPanel {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onLogoutButtonClick
                    ) {
                        Text("Выйти")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onRefreshButtonClick
                    ) {
                        Text("Обновить")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onExitButtonClick
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        }
    ) {
        state.users.forEach { user ->
            UserCard(user)
        }
    }
}