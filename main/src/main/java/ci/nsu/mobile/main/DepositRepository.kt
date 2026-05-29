package ci.nsu.mobile.main

import ci.nsu.mobile.main.data.dao.DepositDao


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