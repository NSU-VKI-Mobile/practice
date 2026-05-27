package ci.nsu.mobile.main.calculations.domain.model

data class RateWithAvailability(
    val rule: RateRule,
    val isAvailable: Boolean,
    val reason: String?
)