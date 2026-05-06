package ci.nsu.mobile.main.util
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val currencyFormatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.getDefault()))
fun Double.formatCurrency(): String = "${currencyFormatter.format(this)} ₽"

fun Long.formatDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}