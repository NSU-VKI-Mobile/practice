package ci.nsu.mobile.main.main.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val russianLocale = Locale.forLanguageTag("ru-RU")

fun formatMoney(value: Double): String {
    return String.format(russianLocale, "%,.2f руб.", value)
}

fun formatPercent(value: Double): String {
    return if (value % 1.0 == 0.0) {
        "${value.toInt()}%"
    } else {
        String.format(russianLocale, "%.2f%%", value)
    }
}

fun formatTopUp(value: Double?): String {
    return if (value == null || value == 0.0) {
        "не указано"
    } else {
        formatMoney(value)
    }
}

fun formatDateTime(timestamp: Long): String {
    return SimpleDateFormat("dd.MM.yyyy HH:mm", russianLocale).format(Date(timestamp))
}
