package ci.nsu.mobile.main.calculations.domain.model

object RateRules {
    val rates = listOf(
        RateRule(
            rate = 15.0,
            minMonths = 1,
            maxMonths = 5,
            description = "15% - для коротких вкладов (до 5 месяцев)"
        ),
        RateRule(
            rate = 10.0,
            minMonths = 6,
            maxMonths = 11,
            description = "10% - для среднесрочных вкладов (6-11 месяцев)"
        ),
        RateRule(
            rate = 12.0,
            minMonths = 1,
            maxMonths = 15,
            description = "дополнительное правило для проверки вывода"
        ),
        RateRule(
            rate = 11.0,
            minMonths = 1,
            maxMonths = 20,
            description = "дополнительное правило для проверки вывода"
        ),
        RateRule(
            rate = 5.0,
            minMonths = 12,
            maxMonths = Int.MAX_VALUE,
            description = "5% - для долгосрочных вкладов (от 12 месяцев)"
        )
    )

    fun getAvailableRates(period: Int): List<RateRule> {
        return rates.filter { rule ->
            period in rule.minMonths..rule.maxMonths
        }
    }

    fun getAllRatesWithAvailability(period: Int): List<RateWithAvailability> {
        return rates.map { rule ->
            RateWithAvailability(
                rule = rule,
                isAvailable = period in rule.minMonths..rule.maxMonths,
                reason = when {
                    period < rule.minMonths -> "Доступно при сроке от ${rule.minMonths} месяцев"
                    period > rule.maxMonths -> "Доступно при сроке до ${rule.maxMonths} месяцев"
                    else -> null
                }
            )
        }
    }
}