package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var isMonthMode by remember { mutableStateOf(true) }

    val shoppingLists by viewModel.shoppingLists.observeAsState(initial = emptyMap())

    var refreshKey by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Заголовок с навигацией
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Верхняя строка: стрелки и дата
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Стрелка влево - как кнопка "Месяц"
                Button(
                    onClick = {
                        if (isMonthMode) {
                            val newCal = currentMonth.clone() as Calendar //копирование
                            newCal.add(Calendar.MONTH, -1) //отнимаем
                            currentMonth = newCal //присваиваем
                        } else {
                            val newCal = currentMonth.clone() as Calendar
                            newCal.add(Calendar.WEEK_OF_YEAR, -1)
                            currentMonth = newCal
                        }
                        refreshKey++
                    },
                    modifier = Modifier.width(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("◀", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                }

                Text(
                    text = if (isMonthMode) {
                        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time)
                    } else {
                        val calendar = currentMonth.clone() as Calendar //копия текущей даты
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) //перевод на неделю с понедельника
                        val start = SimpleDateFormat("dd.MM", Locale.getDefault()).format(calendar.time) //перевод формата
                        calendar.add(Calendar.DAY_OF_MONTH, 6) //плюс 6 дней для воскресенья
                        val end = SimpleDateFormat("dd.MM", Locale.getDefault()).format(calendar.time)
                        "$start - $end"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(horizontal = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Стрелка вправо - как кнопка "Неделя"
                Button(
                    onClick = {
                        if (isMonthMode) {
                            val newCal = currentMonth.clone() as Calendar
                            newCal.add(Calendar.MONTH, 1)
                            currentMonth = newCal
                        } else {
                            val newCal = currentMonth.clone() as Calendar
                            newCal.add(Calendar.WEEK_OF_YEAR, 1)
                            currentMonth = newCal
                        }
                        refreshKey++
                    },
                    modifier = Modifier.width(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("▶", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки переключения режимов
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isMonthMode = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMonthMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        contentColor = if (isMonthMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f) //половина ширины
                ) {
                    Text("Месяц", modifier = Modifier.padding(vertical = 8.dp))
                }

                Button(
                    onClick = { isMonthMode = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isMonthMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        contentColor = if (!isMonthMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Неделя", modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Дни недели
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f), //каждый день занимает одинаковую ширину
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        key(refreshKey) { //обновление
            if (isMonthMode) {
                MonthCalendar(currentMonth, shoppingLists, navController)
            } else {
                WeekCalendar(currentMonth, shoppingLists, navController)
            }
        }
    }
}

@Composable
fun MonthCalendar(
    currentMonth: Calendar,
    shoppingLists: Map<String, List<String>>,
    navController: NavController
) {
    val days = remember(currentMonth, shoppingLists) { getMonthDays(currentMonth, shoppingLists) }

    LazyVerticalGrid( //сетка дат
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { dayInfo ->
            if (dayInfo.day != null) {
                CalendarDayCell(
                    day = dayInfo.day.toString(),
                    hasData = dayInfo.hasData, //есть ли список
                    onClick = { navController.navigate("shopping_list/${dayInfo.dateString}") }
                )
            } else {
                Box(modifier = Modifier.size(48.dp))
            }
        }
    }
}

@Composable
fun WeekCalendar(
    currentMonth: Calendar,
    shoppingLists: Map<String, List<String>>,
    navController: NavController
) {
    val days = remember(currentMonth, shoppingLists) { getWeekDays(currentMonth, shoppingLists) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { dayInfo ->
            CalendarDayCell(
                day = dayInfo.day.toString(),
                hasData = dayInfo.hasData,
                onClick = { navController.navigate("shopping_list/${dayInfo.dateString}") }
            )
        }
    }
}

@Composable
fun CalendarDayCell( //день
    day: String,
    hasData: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = if (hasData) Color(0xFFFFEB3B) else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = day, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

data class CalendarDayInfo(
    val day: Int?,
    val dateString: String,
    val hasData: Boolean
)

fun getMonthDays(currentMonth: Calendar, shoppingLists: Map<String, List<String>>): List<CalendarDayInfo> {
    val days = mutableListOf<CalendarDayInfo>() //пустоц список
    val calendar = currentMonth.clone() as Calendar //копирование даты
    calendar.set(Calendar.DAY_OF_MONTH, 1) //перевод календаря на 1 число

    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) //расчет нчала не пустых ячеек
    val offset = when (firstDayOfWeek) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> 0
    }

    repeat(offset) {
        days.add(CalendarDayInfo(null, "", false))
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //сколько дней в месяце
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day) //установление числа
        val dateString = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-$day" //формирование строки
        days.add(CalendarDayInfo(day, dateString, shoppingLists.containsKey(dateString))) //добавление ячейки в список
    }

    return days
}

fun getWeekDays(currentMonth: Calendar, shoppingLists: Map<String, List<String>>): List<CalendarDayInfo> {
    val days = mutableListOf<CalendarDayInfo>()
    val calendar = currentMonth.clone() as Calendar
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) //формирует неделю с понедельника

    for (i in 0..6) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = "$year-$month-$day"
        days.add(CalendarDayInfo(day, dateString, shoppingLists.containsKey(dateString))) //проверка есть ли список уже на день
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return days
}