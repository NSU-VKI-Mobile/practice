package ci.nsu.mobile.main.presentation.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase
import kotlinx.coroutines.launch
class ResultViewModel(private val repo: DepositRepository) : ViewModel() {

    val result = MutableLiveData<DepositCalculation>()

    fun calculate(a: Double, m: Int, r: Double, t: Double?) {
        val (total, interest) = CalculateDepositUseCase().execute(a, m, r, t)

        result.value = DepositCalculation(
            initialAmount = a,
            periodMonths = m,
            interestRate = r,
            monthlyTopUp = t,
            finalAmount = total,
            interestEarned = interest,
            calculationDate = System.currentTimeMillis()
        )
    }

    fun save() = viewModelScope.launch {
        result.value?.let { repo.insert(it) }
    }
}
