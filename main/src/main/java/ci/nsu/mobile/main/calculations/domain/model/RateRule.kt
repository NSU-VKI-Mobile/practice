package ci.nsu.mobile.main.calculations.domain.model

data class RateRule(
    val rate: Double,
    val minMonths: Int,      // минимальный срок для этой ставки
    val maxMonths: Int,      // максимальный срок
    val description: String
)