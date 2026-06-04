package ci.nsu.mobile.calculations.repository

import ci.nsu.mobile.calculations.data.DepositCalculation
import ci.nsu.mobile.calculations.data.DepositDao

class DepositRepository(
    private val dao: DepositDao
) {

    suspend fun getAllCalculations(): List<DepositCalculation> {
        return dao.getAllCalculations()
    }

    suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return dao.insert(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id)
    }

    suspend fun getCalculationsByUserId(userId: Long): List<DepositCalculation> {
        return dao.getCalculationsByUserId(userId)
    }

    suspend fun deleteCalculationById(id: Long, userId: Long): Boolean {
        return dao.deleteCalculationById(id, userId) > 0
    }
}