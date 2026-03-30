package ci.nsu.mobile.main.data.repositories

import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation

class DepositRepository(private val database: AppDatabase) {

    private val dao = database.depositDao()

    fun getAllCalculations(): List<DepositCalculation> {
        return dao.getAllCalculations()
    }

    fun saveCalculation(calculation: DepositCalculation): Long {
        return dao.insert(calculation)
    }

    fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id)
    }
}