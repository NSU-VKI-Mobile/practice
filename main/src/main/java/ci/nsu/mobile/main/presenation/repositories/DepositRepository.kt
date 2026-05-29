package ci.nsu.mobile.main.presenation.repositories

import ci.nsu.mobile.main.data.dao.DepositDao
import ci.nsu.mobile.main.data.entities.DepositCalculation

class DepositRepository(
    private val dao: DepositDao
) {

    fun getAll() = dao.getAll()

    suspend fun insert(calc: DepositCalculation) {

        dao.insert(calc)
    }

    suspend fun getById(id: Long) =
        dao.getById(id)
}