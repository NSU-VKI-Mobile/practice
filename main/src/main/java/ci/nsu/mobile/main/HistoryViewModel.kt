package ci.nsu.mobile.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = DepositRepository(database.depositDao())

    // Получаем поток всех расчётов (автоматически обновляется)
    val allCalculations: Flow<List<DepositCalculation>> = repository.getAllCalculations() // автоматическое обновление

    // Функция для получения одного расчёта по ID (для деталей)
    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return repository.getCalculationById(id)
    }

    //удаление расчёта
    fun deleteCalculation(calculation: DepositCalculation, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
            onSuccess()
        }
    }
}