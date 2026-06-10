package ci.nsu.mobile.main.data.repository

import android.util.Log
import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    suspend fun saveCalculation(calculation: DepositCalculation) {
        Log.d("DepositRepo", "Saving calculation for userId=${calculation.userId}, amount=${calculation.initialAmount}")
        dao.insertCalculation(calculation)
    }

    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        Log.d("DepositRepo", "Getting calculations for userId=$userId")
        return dao.getCalculationsForUser(userId)
    }

    suspend fun deleteCalculation(calculation: DepositCalculation) {
        dao.deleteCalculation(calculation)
    }
}