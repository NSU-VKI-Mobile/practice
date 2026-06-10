package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel  @Inject constructor (
    private val repository: DepositRepository, private val tokenManager: TokenManager
) : ViewModel() {

    private val _initialAmount = MutableStateFlow("")
    val initialAmount: StateFlow<String> = _initialAmount

    private val _months = MutableStateFlow("")
    val months: StateFlow<String> = _months

    private val _topUp = MutableStateFlow("")
    val topUp: StateFlow<String> = _topUp

    private val _rate = MutableStateFlow(0.0)
    val rate: StateFlow<Double> = _rate

    @OptIn(ExperimentalCoroutinesApi::class)
    val history: Flow<List<DepositEntity>> = flowOf(tokenManager.getUserId()?.toIntOrNull())
        .flatMapLatest { userId ->
            userId?.let {
                repository.getDepositsByUserId(it)
            } ?: flowOf(emptyList())
        }

    fun setInitialAmount(value: String) {
        _initialAmount.value = value
    }

    fun setMonths(value: String) {
        _months.value = value
    }

    fun setTopUp(value: String) {
        _topUp.value = value
    }

    fun calculateRate() {
        val m = _months.value.toIntOrNull() ?: return
        _rate.value = when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }
    fun delete(entity: DepositEntity) {
        viewModelScope.launch {
            repository.delete(entity)
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            tokenManager.getUserId()?.toIntOrNull()?.let {
                repository.deleteDepositsByUserId(it)
            }
        }
    }
    fun calculateResult(): Pair<Double, Double> {
        val initial = _initialAmount.value.toDoubleOrNull() ?: 0.0
        val months = _months.value.toIntOrNull() ?: 0
        val rate = _rate.value / 100
        val topUp = _topUp.value.toDoubleOrNull() ?: 0.0

        var total = initial

        repeat(months) {
            total += total * rate / 12
            total += topUp
        }

        val interest = total - initial - (topUp * months)

        return total to interest
    }

    fun save() {
        val (total, interest) = calculateResult()
        val uId = tokenManager.getUserId()?.toIntOrNull() ?: return

        val entity = DepositEntity(
            initialAmount = _initialAmount.value.toDoubleOrNull() ?: 0.0,
            months = _months.value.toIntOrNull() ?: 0,
            rate = _rate.value,
            topUp = _topUp.value.toDoubleOrNull() ?: 0.0,
            finalAmount = total,
            interest = interest,
            userId = uId,
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insert(entity)
        }
    }

    fun setRate(toDouble: Double) {}
}