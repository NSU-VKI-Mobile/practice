package ci.nsu.mobile.main.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.io.Serializable


// --------------------------------------------------------------------------
// Главный экран с переключателем режимов
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var isWeekView by remember { mutableStateOf(false) }
    var currentMonthDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val notes = remember { mutableStateMapOf<LocalDate, String>() }
    val context = LocalContext.current
    data class NoteItem(
        val id: Int,
        val text: String,
        val isCompleted: Boolean = false
    ) : Serializable

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isWeekView = !isWeekView },
                containerColor = MaterialTheme.colorScheme.primary
            ) {

            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (!isWeekView) {
                MonthView(
                    currentDate = currentMonthDate,
                    selectedDate = selectedDate,
                    notes = notes,
                    onDateSelected = { date ->
                        selectedDate = date
                        Toast.makeText(
                            context,
                            "Выбрано: ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onNoteChange = { date, text -> notes[date] = text },
                    onPrevMonth = { currentMonthDate = currentMonthDate.minusMonths(1) },
                    onNextMonth = { currentMonthDate = currentMonthDate.plusMonths(1) }
                )
            } else {
                WeekView(
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        Toast.makeText(
                            context,
                            "Выбрано: ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    notes = notes,
                    onNoteChange = { date, text -> notes[date] = text }
                )
            }
        }
    }
}

// --------------------------------------------------------------------------
// РЕЖИМ МЕСЯЦА (ваш старый код, слегка адаптированный)
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthView(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    notes: Map<LocalDate, String>,      // новый параметр
    onDateSelected: (LocalDate) -> Unit,
    onNoteChange: (LocalDate, String) -> Unit,  // для редактирования
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    // Состояние для диалога заметки
    var editingDate by remember { mutableStateOf<LocalDate?>(null) }
    var draftText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Шапка месяца
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevMonth) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущий месяц")
            }
            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("LLLL yyyy").withLocale(Locale("ru"))),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNextMonth) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Следующий месяц")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Дни недели
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // Сетка дней месяца
        MonthCalendarGrid(
            currentDate = currentDate,
            selectedDate = selectedDate,
            notes = notes,
            onDateSelected = { date ->
                editingDate = date
                draftText = notes[date] ?: ""
            }
        )
    }

    // Диалог редактирования заметки
    editingDate?.let { date ->
        AlertDialog(
            onDismissRequest = { editingDate = null },
            title = { Text("Заметка на ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}") },
            text = {
                OutlinedTextField(
                    value = draftText,
                    onValueChange = { draftText = it },
                    label = { Text("Текст заметки") },
                    minLines = 5,
                    maxLines = 10
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onNoteChange(date, draftText)
                    onDateSelected(date)  // обновляем выбранную дату
                    editingDate = null
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingDate = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun WeekDaysHeader() {
    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCalendarGrid(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    notes: Map<LocalDate, String>,  // новый параметр
    onDateSelected: (LocalDate) -> Unit
) {
    val days = getDaysOfMonth(currentDate)

    Column(modifier = Modifier.fillMaxSize()) {
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isCurrentMonth = date.month == currentDate.month,
                        hasNote = notes[date]?.isNotBlank() == true,  // проверяем наличие заметки
                        onClick = { onDateSelected(date) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (week.size < 7) {
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Ячейка дня (общая для месяца и недели)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    hasNote: Boolean,        // новый параметр
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = date == LocalDate.now()
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        isToday -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        !isCurrentMonth -> Color.Gray
        isToday -> MaterialTheme.colorScheme.primary
        else -> Color.Black
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
            )
            // Индикатор заметки (маленькая точка или иконка)
            if (hasNote) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFFF69B4))
                )
            }
        }
    }
}

// Формирует список из 42 дней для сетки месяца (дни предыдущего/текущего/следующего месяца)
@RequiresApi(Build.VERSION_CODES.O)
fun getDaysOfMonth(currentDate: LocalDate): List<LocalDate> {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val daysFromPrevMonth = (firstDayOfWeek - 1 + 7) % 7

    val result = mutableListOf<LocalDate>()
    val prevMonth = currentDate.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()
    for (i in daysFromPrevMonth downTo 1) {
        result.add(prevMonth.withDayOfMonth(daysInPrevMonth - i + 1))
    }
    val daysInCurrentMonth = currentDate.lengthOfMonth()
    for (i in 1..daysInCurrentMonth) {
        result.add(currentDate.withDayOfMonth(i))
    }
    val remainingCells = 42 - result.size
    val nextMonth = currentDate.plusMonths(1)
    for (i in 1..remainingCells) {
        result.add(nextMonth.withDayOfMonth(i))
    }
    return result
}

// --------------------------------------------------------------------------
// РЕЖИМ НЕДЕЛИ с заметками
// --------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    notes: Map<LocalDate, String>,
    onNoteChange: (LocalDate, String) -> Unit
) {
    var currentWeekStart by remember { mutableStateOf(getWeekStart(selectedDate)) }

    LaunchedEffect(selectedDate) {
        val newStart = getWeekStart(selectedDate)
        if (newStart != currentWeekStart) {
            currentWeekStart = newStart
        }
    }

    val daysOfWeek = remember(currentWeekStart) { getDaysOfWeek(currentWeekStart) }

    // Состояние для диалога заметки
    var editingDate by remember { mutableStateOf<LocalDate?>(null) }
    var draftText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = formatWeekRange(currentWeekStart)) },
                navigationIcon = {
                    IconButton(onClick = { currentWeekStart = currentWeekStart.minusWeeks(1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущая неделя")
                    }
                },
                actions = {
                    IconButton(onClick = { currentWeekStart = currentWeekStart.plusWeeks(1) }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Следующая неделя")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(daysOfWeek.size) { index ->
                val date = daysOfWeek[index]
                val dayName = date.format(DateTimeFormatter.ofPattern("EEEE", Locale("ru")))
                    .replaceFirstChar { it.uppercase() }
                val dayNumber = date.format(DateTimeFormatter.ofPattern("dd.MM", Locale("ru")))
                val note = notes[date] ?: ""

                WeekDayRow(
                    date = date,
                    dayName = dayName,
                    dayNumber = dayNumber,
                    note = note,
                    isSelected = date == selectedDate,
                    onDayClick = { onDateSelected(date) },
                    onNoteClick = {
                        editingDate = date
                        draftText = note
                    }
                )
            }
        }
    }

    // Диалог редактирования заметки (вынесен из списка)
    editingDate?.let { date ->
        AlertDialog(
            onDismissRequest = { editingDate = null },
            title = { Text("Заметка на ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}") },
            text = {
                OutlinedTextField(
                    value = draftText,
                    onValueChange = { draftText = it },
                    label = { Text("Текст заметки") },
                    minLines = 3,
                    maxLines = 6
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onNoteChange(date, draftText)
                    editingDate = null
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingDate = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekDayRow(
    date: LocalDate,
    dayName: String,
    dayNumber: String,
    note: String,
    isSelected: Boolean,
    onDayClick: () -> Unit,
    onNoteClick: () -> Unit   // теперь без параметров
) {
    val isToday = date == LocalDate.now()
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    else if (isToday) MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
    else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDayClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.35f)) {
                Text(
                    text = dayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = dayNumber,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .clickable { onNoteClick() }
                    .padding(4.dp)
            ) {
                if (note.isBlank()) {
                    Text(
                        text = "➕ Добавить заметку",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    Text(
                        text = note,
                        fontSize = 14.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// Вспомогательные функции для работы с неделями
@RequiresApi(Build.VERSION_CODES.O)
fun getWeekStart(date: LocalDate): LocalDate {
    // Понедельник = первый день недели
    val dayOfWeek = date.dayOfWeek.value  // 1 = понедельник, 7 = воскресенье
    return date.minusDays((dayOfWeek - 1).toLong())
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysOfWeek(weekStart: LocalDate): List<LocalDate> {
    return (0..6).map { weekStart.plusDays(it.toLong()) }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatWeekRange(weekStart: LocalDate): String {
    val weekEnd = weekStart.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
    return "${weekStart.format(formatter)} – ${weekEnd.format(formatter)}"
}