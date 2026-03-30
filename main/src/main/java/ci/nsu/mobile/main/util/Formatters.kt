package ci.nsu.mobile.main.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
    maximumFractionDigits = 2
    minimumFractionDigits = 0
}

fun formatAmount(value: Double): String = numberFormat.format(value)

fun formatDateTime(epochMillis: Long): String {
    val fmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return fmt.format(Date(epochMillis))
}

