package ci.nsu.mobile.main.utils

fun Double.formatToCurrency(): String {
    return java.text.NumberFormat.getCurrencyInstance(java.util.Locale("ru", "RU")).format(this)
}