package ci.nsu.ui.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.data.local.SessionManager
import ci.nsu.data.model.DepositCalculation
import ci.nsu.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class DepositViewModel(
    private val depositRepository: DepositRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Стрим со списком расчетов текущего пользователя для экрана "Мои расчёты"
    private val _userCalculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val userCalculations: StateFlow<List<DepositCalculation>> = _userCalculations.asStateFlow()

    init {
        loadCalculations()
    }

    // Загрузка расчетов конкретного пользователя
    fun loadCalculations() {
        val currentUserId = sessionManager.getUserId()
        if (currentUserId != -1L) {
            viewModelScope.launch {
                depositRepository.getCalculationsForUser(currentUserId).collect { list ->
                    _userCalculations.value = list
                }
            }
        }
    }

    // Метод двухэтапного расчета и сохранения (Бизнес-логика калькулятора)
    fun calculateAndSave(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ) {
        val currentUserId = sessionManager.getUserId()
        if (currentUserId == -1L) return // Пользователь не авторизован

        viewModelScope.launch {
            // Простейшая формула сложных процентов с ежемесячным пополнением
            var finalAmount = initialAmount
            val monthlyRate = (interestRate / 100) / 12
            val topUp = monthlyTopUp ?: 0.0

            for (i in 1..periodMonths) {
                finalAmount += topUp // Ежемесячное пополнение
                finalAmount *= (1 + monthlyRate) // Начисление процентов
            }

            val totalInvested = initialAmount + (topUp * periodMonths)
            val interestEarned = finalAmount - totalInvested

            // Формируем сущность для Room с привязкой к userId
            val calculation = DepositCalculation(
                userId = currentUserId,
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = Math.round(finalAmount * 100) / 100.0, // Округление до 2 знаков
                interestEarned = Math.round(interestEarned * 100) / 100.0,
                calculationDate = Date().time
            )

            // Сохраняем в базу данных
            depositRepository.insertCalculation(calculation)
        }
    }

    // Удаление расчета из истории
    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            depositRepository.deleteCalculation(calculation)
        }
    }
}
