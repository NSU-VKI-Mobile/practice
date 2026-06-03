package ci.nsu.mobile.main.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.NotificationItem
import ci.nsu.mobile.main.utils.DateUtils
import ci.nsu.mobile.main.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: NotificationViewModel,
    notificationId: Long?,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(Calendar.getInstance()) }
    var isEditMode by remember { mutableStateOf(notificationId != null) }
    var originalNotification by remember { mutableStateOf<NotificationItem?>(null) }
    var showConflictDialog by remember { mutableStateOf(false) }
    var pendingTimestamp by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(notificationId) {
        if (notificationId != null) {
            viewModel.notifications.value.find { it.id == notificationId }?.let {
                originalNotification = it
                title = it.title
                description = it.description
                val calendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                selectedDate = calendar
                selectedTime = calendar
                isEditMode = true
            }
        }
    }

    fun performSave(timestamp: Long) {
        if (isEditMode && originalNotification != null) {
            val updatedNotification = originalNotification!!.copy(
                title = title,
                description = description,
                timestamp = timestamp
            )
            viewModel.updateNotification(updatedNotification) {
                onNavigateBack()
            }
        } else {
            val newNotification = NotificationItem(
                title = title,
                description = description,
                timestamp = timestamp
            )
            viewModel.addNotification(newNotification) {
                onNavigateBack()
            }
        }
    }

    val hasChanges = if (isEditMode && originalNotification != null) {
        val newTimestamp = DateUtils.combineDateTime(selectedDate, selectedTime)
        title != originalNotification!!.title ||
                description != originalNotification!!.description ||
                newTimestamp != originalNotification!!.timestamp
    } else {
        title.isNotBlank() && description.isNotBlank()
    }

    fun saveNotification(force: Boolean = false) {
        val timestamp = DateUtils.combineDateTime(selectedDate, selectedTime)

        if (!force && !isEditMode) {
            coroutineScope.launch {
                val exists = viewModel.checkExistsAtTimestamp(timestamp)
                if (exists) {
                    pendingTimestamp = timestamp
                    showConflictDialog = true
                    return@launch
                }
                performSave(timestamp)
            }
        } else {
            performSave(timestamp)
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
        },
        selectedDate.get(Calendar.YEAR),
        selectedDate.get(Calendar.MONTH),
        selectedDate.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
        },
        selectedTime.get(Calendar.HOUR_OF_DAY),
        selectedTime.get(Calendar.MINUTE),
        true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Редактирование" else "Новое уведомление") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Дата и время уведомления",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(DateUtils.formatDate(selectedDate.timeInMillis))
                        }

                        Button(
                            onClick = { timePickerDialog.show() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(DateUtils.formatTime(selectedTime.timeInMillis))
                        }
                    }

                    Text(
                        text = "Выбранное время: ${DateUtils.formatDateTime(DateUtils.combineDateTime(selectedDate, selectedTime))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { saveNotification() },
                modifier = Modifier.fillMaxWidth(),
                enabled = hasChanges
            ) {
                Text(if (isEditMode) "Применить" else "Создать")
            }
        }
    }

    if (showConflictDialog) {
        AlertDialog(
            onDismissRequest = { showConflictDialog = false },
            title = { Text("Уведомление уже существует") },
            text = { Text("На это время уже запланировано уведомление. Создать всё равно?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConflictDialog = false
                        pendingTimestamp?.let {
                            performSave(it)
                        }
                    }
                ) {
                    Text("Создать")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConflictDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}