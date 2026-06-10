package ci.nsu.mobile.main.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlin.math.max
import kotlin.math.min

@Composable
fun HomeScreen(
    viewModel: UsersViewModel,
    onLogout: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = if (searchQuery.isBlank()) {
        viewModel.users
    } else {
        viewModel.users.filter { user ->
            user.login?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Переменные для скроллбара
    var boxHeight by remember { mutableStateOf(0f) }
    var thumbHeight by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    // Вычисляем высоту бегунка пропорционально видимой части списка
    val totalItems = filteredUsers.size
    val visibleItems = (listState.layoutInfo.visibleItemsInfo.size)
    val thumbRatio = if (totalItems > 0) visibleItems.toFloat() / totalItems else 1f
    val targetThumbHeight = (boxHeight * thumbRatio).coerceAtLeast(20f)

    // Позиция бегунка (от 0 до boxHeight - thumbHeight)
    val firstVisibleIndex = listState.firstVisibleItemIndex
    val scrollOffset = listState.firstVisibleItemScrollOffset
    val totalScrollRange = max(0, totalItems - visibleItems)
    val thumbPosition = if (totalScrollRange > 0) {
        (firstVisibleIndex.toFloat() + scrollOffset / 1000f) / totalScrollRange * (boxHeight - thumbHeight)
    } else 0f

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Выйти")
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск по логину") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        if (viewModel.loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { layoutCoordinates ->
                        boxHeight = layoutCoordinates.size.height.toFloat()
                    }
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredUsers) { user ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Логин: ${user.login ?: "null"}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = "Email: ${user.email ?: "null"}")
                                Text(text = "Телефон: ${user.phoneNumber ?: "не указан"}")
                                Text(text = "Role ID: ${user.roleId ?: "null"}")
                                Text(text = "Person ID: ${user.personId ?: "null"}")
                                Text(text = "Авторизация: ${if (user.authAllowed == true) "Разрешена" else "Запрещена"}")
                                Text(text = "Дата создания: ${user.createdDate?.substringBefore('T') ?: "не указана"}")
                                Text(text = "Последний вход: ${user.lastLoginDate?.substringBefore('T') ?: "не указан"}")
                            }
                        }
                    }
                }
            }
        }
    }
}