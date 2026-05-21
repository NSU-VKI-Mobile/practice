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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Текущая отображаемая дата (год и месяц)
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    // Выбранная пользователем дата (по умолчанию сегодня)
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Шапка с навигацией
        CalendarHeader(
            currentDate = currentDate,
            onPrevMonth = { currentDate = currentDate.minusMonths(1) },
            onNextMonth = { currentDate = currentDate.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Дни недели
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // Сетка дней месяца
        CalendarGrid(
            currentDate = currentDate,
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                Toast.makeText(
                    context,
                    "Выбрано: ${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}

// Шапка с названием месяца и кнопками
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    currentDate: LocalDate,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
}

// Строка с названиями дней недели (начинается с понедельника)
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

// Сетка дней месяца (6 строк × 7 столбцов)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = getDaysOfMonth(currentDate)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Разбиваем список дней на недели (по 7 элементов)
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isCurrentMonth = date.month == currentDate.month,
                        onClick = { onDateSelected(date) }
                    )
                }
                // Если в последней неделе меньше 7 дней, добавляем пустые ячейки
                if (week.size < 7) {
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Ячейка с одним днём
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
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
        modifier = Modifier

            .aspectRatio(1f) // квадратная ячейка
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

// Возвращает список всех дней, которые нужно отобразить в сетке (включая дни предыдущего и следующего месяца)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getDaysOfMonth(currentDate: LocalDate): List<LocalDate> {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    // День недели первого дня месяца (понедельник = 1, воскресенье = 7)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // понедельник = 1 (в Java.Time)
    // Сколько дней нужно добавить из предыдущего месяца, чтобы сетка начиналась с понедельника
    val daysFromPrevMonth = (firstDayOfWeek - 1 + 7) % 7

    val result = mutableListOf<LocalDate>()

    // Добавляем дни предыдущего месяца
    val prevMonth = currentDate.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()
    for (i in daysFromPrevMonth downTo 1) {
        result.add(prevMonth.withDayOfMonth(daysInPrevMonth - i + 1))
    }

    // Добавляем дни текущего месяца
    val daysInCurrentMonth = currentDate.lengthOfMonth()
    for (i in 1..daysInCurrentMonth) {
        result.add(currentDate.withDayOfMonth(i))
    }

    // Добавляем дни следующего месяца, чтобы заполнить сетку до 6 строк (42 дня)
    val remainingCells = 42 - result.size
    val nextMonth = currentDate.plusMonths(1)
    for (i in 1..remainingCells) {
        result.add(nextMonth.withDayOfMonth(i))
    }

    return result
}




