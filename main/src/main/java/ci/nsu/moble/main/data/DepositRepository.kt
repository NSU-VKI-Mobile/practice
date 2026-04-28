package ci.nsu.moble.main.data

import kotlinx.coroutines.flow.Flow

// Repository — прослойка между ViewModel и базой данных.
// ViewModel не знает откуда берутся данные — из БД, сети или ещё откуда-то.
class DepositRepository(private val dao: DepositDao) {

    // История расчётов — обновляется автоматически при изменении БД
    val allDeposits: Flow<List<DepositCalculation>> = dao.getAll()

    suspend fun insert(deposit: DepositCalculation) {
        dao.insert(deposit)
    }

    suspend fun getById(id: Long): DepositCalculation? {
        return dao.getById(id)
    }
    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}