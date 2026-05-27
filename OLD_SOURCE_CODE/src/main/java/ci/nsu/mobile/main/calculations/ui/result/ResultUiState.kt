package ci.nsu.mobile.main.calculations.ui.result

data class ResultUiState(
    val initialAmount: Double = 0.0,
    val term: Int = 0,
    val rate: Double = 0.0,
    val monthlyAddition: Double = 0.0,
    val finalAmount: Double = 0.0,
    val earnedInterest: Double = 0.0,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)