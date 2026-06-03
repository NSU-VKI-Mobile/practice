@file:OptIn(ExperimentalFoundationApi::class)

package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.model.NotificationItem
import ci.nsu.mobile.main.utils.DateUtils
import ci.nsu.mobile.main.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NotificationViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var notificationToDelete by remember { mutableStateOf<NotificationItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Планировщик уведомлений") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Количество уведомлений: ${notifications.size}",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (notifications.isEmpty()) {
                    Text(
                        text = "Нет уведомлений",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Нажмите + чтобы добавить",
                        fontSize = 14.sp,
                        color = Color(0xFF6200EE)
                    )
                } else {
                    LazyColumn {
                        items(notifications) { notification ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .combinedClickable(
                                        onClick = { onEditClick(notification.id) },
                                        onLongClick = {
                                            notificationToDelete = notification
                                            showDeleteDialog = true
                                        }
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFC7BDBD)
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = notification.title,
                                            fontSize = 16.sp,
                                            color = if (notification.isEnabled) Color.Black else Color.DarkGray
                                        )
                                        Text(
                                            text = notification.description,
                                            fontSize = 12.sp,
                                            color = if (notification.isEnabled) Color.DarkGray else Color.DarkGray.copy(alpha = 0.6f)
                                        )
                                    }

                                    // Отображение времени уведомления
                                    Text(
                                        text = DateUtils.formatDateTime(notification.timestamp),
                                        fontSize = 12.sp,
                                        color = if (notification.isEnabled) MaterialTheme.colorScheme.primary else Color.DarkGray,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    Checkbox(
                                        checked = notification.isEnabled,
                                        onCheckedChange = { viewModel.toggleEnabled(notification) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && notificationToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                notificationToDelete = null
            },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы действительно хотите удалить напоминание \"${notificationToDelete!!.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        notificationToDelete?.let {
                            viewModel.deleteNotification(it)
                        }
                        showDeleteDialog = false
                        notificationToDelete = null
                    }
                ) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        notificationToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}