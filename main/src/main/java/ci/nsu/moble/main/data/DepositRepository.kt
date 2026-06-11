// Task_5: Repository — абстракция над Room. Предоставляет API для ViewModel.

package ci.nsu.moble.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DepositRepository(private val dao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> = flow {
        emit(dao.getAll())
    }

    suspend fun getCalculation(id: Long): DepositCalculation? {
        return dao.getById(id)
    }

    suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return dao.insert(calculation)
    }
    suspend fun deleteCalculation(id: Long) =
        dao.deleteCalculation(id)
}
