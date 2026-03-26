package ci.nsu.mobile.main.domain.model

data class RateWithAvailability(
    val rule: RateRule,
    val isAvailable: Boolean,
    val reason: String?
)
