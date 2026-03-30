package ci.nsu.mobile.main.data.repository

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.db.DepositCalculationDao
import ci.nsu.mobile.main.data.db.DepositCalculationEntity

class DepositRepository(
    private val dao: DepositCalculationDao
) {
    fun observeAllCalculations(): LiveData<List<DepositCalculationEntity>> = dao.observeAll()
    fun observeCalculationById(id: Long): LiveData<DepositCalculationEntity?> = dao.observeById(id)

    suspend fun insertCalculation(entity: DepositCalculationEntity): Long = dao.insert(entity)
}

