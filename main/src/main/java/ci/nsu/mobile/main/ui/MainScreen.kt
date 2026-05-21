package ci.nsu.mobile.main.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale





// --------------------------------------------------------------------------
// Главный экран с переключателем режимов
// --------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Режим отображения: false = месяц, true = неделя
    var isWeekView by remember { mutableStateOf(false) }

    // Текущая дата для месячного режима
    var currentMonthDate by remember { mutableStateOf(LocalDate.now()) }
    // Выбранная пользователем дата (общая для обоих режимов)
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Заметки: ключ = дата, значение = текст заметки
    val notes = remember { mutableStateMapOf<LocalDate, String>() }

    val context = LocalContext.current

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
                // -------------------- РЕЖИМ МЕСЯЦА --------------------
                MonthView(
                    currentDate = currentMonthDate,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        Toast.makeText(
                            context,
                            "Выбрано: ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onPrevMonth = { currentMonthDate = currentMonthDate.minusMonths(1) },
                    onNextMonth = { currentMonthDate = currentMonthDate.plusMonths(1) }
                )
            } else {
                // -------------------- РЕЖИМ НЕДЕЛИ --------------------
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
                    onNoteChange = { date, newText -> notes[date] = newText }
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
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
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

        // Дни недели (пн-вс)
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // Сетка дней месяца
        MonthCalendarGrid(
            currentDate = currentDate,
            selectedDate = selectedDate,
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
    onDateSelected: (LocalDate) -> Unit
) {
    val days = getDaysOfMonth(currentDate)  // теперь это обычная функция

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
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    notes: Map<LocalDate, String>,
    onNoteChange: (LocalDate, String) -> Unit
) {
    // Вычисляем начало недели (понедельник) для выбранной даты
    val weekStart = remember(selectedDate) { getWeekStart(selectedDate) }
    var currentWeekStart by remember { mutableStateOf(weekStart) }

    // Если selectedDate поменялась извне, корректируем отображаемую неделю
    LaunchedEffect(selectedDate) {
        val newStart = getWeekStart(selectedDate)
        if (newStart != currentWeekStart) {
            currentWeekStart = newStart
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Шапка недели с навигацией
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentWeekStart = currentWeekStart.minusWeeks(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущая неделя")
            }
            Text(
                text = formatWeekRange(currentWeekStart),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentWeekStart = currentWeekStart.plusWeeks(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Следующая неделя")
            }
        }

        // Строка с днями недели
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Горизонтальные ячейки дней недели (с датами)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = getDaysOfWeek(currentWeekStart)
            daysOfWeek.forEach { date ->
                DayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    isCurrentMonth = true, // в неделе все дни визуально одинаковы
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Блок заметок для выбранного дня
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Заметки на ${selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes[selectedDate] ?: "",
                    onValueChange = { onNoteChange(selectedDate, it) },
                    placeholder = { Text("Введите заметку...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
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