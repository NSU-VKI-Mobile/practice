package ci.nsu.mobile.main.data


object InterestRates {

    private val rates = mapOf(
        15.0 to 5,
        10.0 to 9,
        5.0 to 12
    )


    fun getAvailableRates(): List<Pair<Double, Int>> = rates.toList()


    fun getDefaultRate(months: Int): Double = when {
        months < 6 -> 15.0
        months < 12 -> 10.0
        else -> 5.0
    }


    fun getDefaultPeriod(months: Int): Int = when {
        months < 6 -> 5
        months < 12 -> 9
        else -> 12
    }


    fun getPeriodForRate(rate: Double): Int = rates[rate] ?: 12
}