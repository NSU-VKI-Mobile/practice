package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    val allDeposits: Flow<List<Deposit>> = dao.getAllDeposits()

    fun getDepositById(id: Int): Flow<Deposit?> = dao.getDepositById(id)

    suspend fun insert(deposit: Deposit) {
        dao.insert(deposit)
    }

    // НОВОЕ:
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}