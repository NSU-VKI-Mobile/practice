package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.database.DepositDao

class DepositRepository(
    private val dao: DepositDao
) {

    suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return dao.insert(calculation)
    }

    suspend fun getCalculationsByUserId(userId: Long): List<DepositCalculation> {
        return dao.getCalculationsByUserId(userId)
    }

    suspend fun getCalculationById(id: Long, userId: Long): DepositCalculation? {
        return dao.getCalculationById(id, userId)
    }

    suspend fun deleteCalculationById(id: Long, userId: Long): Boolean {
        return dao.deleteCalculationById(id, userId) > 0
    }
}