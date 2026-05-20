package ci.nsu.moble.main

class DepositRepository(private val dao: DepositDao) {

    val allDeposits = dao.getAll()

    suspend fun insert(entity: DepositEntity) {
        dao.insert(entity)
    }

    suspend fun delete(entity: DepositEntity) {
        dao.delete(entity)
    }
}