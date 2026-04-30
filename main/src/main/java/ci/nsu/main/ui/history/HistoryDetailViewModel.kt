package ci.nsu.mobile.main.ui.history

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.model.DepositCalculation

class HistoryDetailViewModel : ViewModel() {

    private val repository = DepositApplication.getInstance().repository

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return try {
            repository.getCalculationById(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteCalculation(id: Long): Boolean {
        return try {
            val calculation = repository.getCalculationById(id)
            if (calculation != null) {
                repository.deleteCalculation(calculation)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}