package ci.nsu.mobile.main.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.data.NoteDatabase
import ci.nsu.mobile.main.ui.data.NoteEntity
import ci.nsu.mobile.main.ui.data.NoteRepository
import ci.nsu.mobile.main.ui.data.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// --------------------------------------------------------------------------
// Фабрика для ViewModel
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
fun provideNoteViewModel(context: android.content.Context): NoteViewModel {
    val database = NoteDatabase.getDatabase(context)
    val repository = NoteRepository(database.noteDao())
    return NoteViewModel(repository)
}

// --------------------------------------------------------------------------
// Главный экран с навигацией
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Состояние календаря
    var isWeekView by remember { mutableStateOf(false) }
    var currentMonthDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // ViewModel для заметок
    val noteViewModel = remember { provideNoteViewModel(context) }

    // Счётчики заметок для каждого дня в месяце (обновляются при загрузке)
    var notesCountMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }
    var completedCountMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }

    // Загружаем счётчики для текущего месяца
    LaunchedEffect(currentMonthDate) {
        val firstDay = currentMonthDate.withDayOfMonth(1)
        val lastDay = currentMonthDate.withDayOfMonth(currentMonthDate.lengthOfMonth())

        val counts = mutableMapOf<LocalDate, Int>()
        val completed = mutableMapOf<LocalDate, Int>()

        var current = firstDay
        while (current <= lastDay) {
            counts[current] = noteViewModel.getNoteCount(current)
            completed[current] = noteViewModel.getCompletedCount(current)
            current = current.plusDays(1)
        }
        notesCountMap = counts
        completedCountMap = completed
    }

    NavHost(
        navController = navController,
        startDestination = "calendar"
    ) {
        composable("calendar") {
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
                            notesCountMap = notesCountMap,
                            completedCountMap = completedCountMap,
                            onDateSelected = { date ->
                                selectedDate = date
                                navController.navigate("notes/${date}")
                            },
                            onPrevMonth = {
                                currentMonthDate = currentMonthDate.minusMonths(1)
                            },
                            onNextMonth = {
                                currentMonthDate = currentMonthDate.plusMonths(1)
                            }
                        )
                    } else {
                        WeekView(
                            selectedDate = selectedDate,
                            noteViewModel = noteViewModel,
                            onDateSelected = { date ->
                                selectedDate = date
                                navController.navigate("notes/${date}")
                            }
                        )
                    }
                }
            }
        }

        composable("notes/{date}") { backStackEntry ->
            val dateStr = backStackEntry.arguments?.getString("date")
            val date = LocalDate.parse(dateStr)

            NoteScreen(
                date = date,
                viewModel = noteViewModel,
                onBack = {
                    // Обновляем счётчики при возврате
                    CoroutineScope(Dispatchers.Main).launch {
                        val count = noteViewModel.getNoteCount(date)
                        val completed = noteViewModel.getCompletedCount(date)
                        notesCountMap = notesCountMap.toMutableMap().apply {
                            this[date] = count
                        }
                        completedCountMap = completedCountMap.toMutableMap().apply {
                            this[date] = completed
                        }
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}

// --------------------------------------------------------------------------
// РЕЖИМ МЕСЯЦА
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthView(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    notesCountMap: Map<LocalDate, Int>,
    completedCountMap: Map<LocalDate, Int>,
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
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

        WeekDaysHeader()

        Spacer(modifier = Modifier.height(4.dp))

        MonthCalendarGrid(
            currentDate = currentDate,
            selectedDate = selectedDate,
            notesCountMap = notesCountMap,
            completedCountMap = completedCountMap,
            onDateSelected = onDateSelected
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
    notesCountMap: Map<LocalDate, Int>,
    completedCountMap: Map<LocalDate, Int>,
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
                    val noteCount = notesCountMap[date] ?: 0
                    val completedCount = completedCountMap[date] ?: 0

                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isCurrentMonth = date.month == currentDate.month,
                        noteCount = noteCount,
                        completedCount = completedCount,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    noteCount: Int,
    completedCount: Int,
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

            // Индикаторы заметок
            if (noteCount > 0) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    if (completedCount > 0) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Выполнено",
                            modifier = Modifier.size(8.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (noteCount > completedCount) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFFF69B4)
                                )
                        )
                    }
                    Text(
                        text = "$noteCount",
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
    }
}

// --------------------------------------------------------------------------
// РЕЖИМ НЕДЕЛИ
// --------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(
    selectedDate: LocalDate,
    noteViewModel: NoteViewModel,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentWeekStart by remember { mutableStateOf(getWeekStart(selectedDate)) }
    var notesMap by remember { mutableStateOf<Map<LocalDate, List<NoteEntity>>>(emptyMap()) }

    LaunchedEffect(selectedDate, currentWeekStart) {
        val newStart = getWeekStart(selectedDate)
        if (newStart != currentWeekStart) {
            currentWeekStart = newStart
        }

        // Загружаем заметки для всех дней недели
        val days = getDaysOfWeek(currentWeekStart)
        val map = mutableMapOf<LocalDate, List<NoteEntity>>()
        for (day in days) {
            map[day] = noteViewModel.getNotesSync(day)
        }
        notesMap = map
    }

    val daysOfWeek = remember(currentWeekStart) { getDaysOfWeek(currentWeekStart) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = formatWeekRange(currentWeekStart)) },
                navigationIcon = {
                    IconButton(onClick = {
                        currentWeekStart = currentWeekStart.minusWeeks(1)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущая неделя")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        currentWeekStart = currentWeekStart.plusWeeks(1)
                    }) {
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
                val notes = notesMap[date] ?: emptyList()
                val notesPreview = notes.take(2).joinToString(", ") { it.text }
                val completedCount = notes.count { it.isCompleted }

                WeekDayRow(
                    date = date,
                    dayName = dayName,
                    dayNumber = dayNumber,
                    notesPreview = notesPreview,
                    hasNotes = notes.isNotEmpty(),
                    completedCount = completedCount,
                    isSelected = date == selectedDate,
                    onDayClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekDayRow(
    date: LocalDate,
    dayName: String,
    dayNumber: String,
    notesPreview: String,
    hasNotes: Boolean,
    completedCount: Int,
    isSelected: Boolean,
    onDayClick: () -> Unit
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
                if (completedCount > 0) {
                    Text(
                        text = "✓ $completedCount выполнено",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .padding(4.dp)
            ) {
                if (!hasNotes) {
                    Text(
                        text = "➕ Добавить заметку",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    Text(
                        text = notesPreview,
                        fontSize = 14.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// --------------------------------------------------------------------------
// ЭКРАН ЗАМЕТОК
// --------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen(
    date: LocalDate,
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    var newNoteText by remember { mutableStateOf("") }
    val notes by viewModel.notes.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))

    // Загружаем заметки при открытии
    LaunchedEffect(date) {
        viewModel.loadNotes(date)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметки на ${date.format(dateFormatter)}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Поле добавления заметки
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = { newNoteText = it },
                        label = { Text("Новая заметка") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newNoteText.isNotBlank()) {
                                viewModel.addNote(date, newNoteText) {
                                    newNoteText = ""
                                    viewModel.loadNotes(date)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список заметок
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет заметок.\nДобавьте первую заметку!",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                Text(
                    text = "Мои заметки (${notes.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        NoteItemCard(
                            note = note,
                            onToggleComplete = {
                                viewModel.toggleNoteCompletion(note) {
                                    viewModel.loadNotes(date)
                                }
                            },
                            onDelete = {
                                viewModel.deleteNote(note) {
                                    viewModel.loadNotes(date)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItemCard(
    note: NoteEntity,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = note.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = note.text,
                    fontSize = 16.sp,
                    textDecoration = if (note.isCompleted) TextDecoration.LineThrough else null,
                    color = if (note.isCompleted) Color.Gray else Color.Black
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// --------------------------------------------------------------------------
// ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
// --------------------------------------------------------------------------
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

@RequiresApi(Build.VERSION_CODES.O)
fun getWeekStart(date: LocalDate): LocalDate {
    val dayOfWeek = date.dayOfWeek.value
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