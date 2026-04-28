package ci.nsu.mobile.main

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    val allDeposits: Flow<List<DepositCalculation>> = dao.getAllDeposits()

    suspend fun insert(deposit: DepositCalculation): Long {
        return dao.insert(deposit)
    }

    suspend fun getDepositById(id: Long): DepositCalculation? {
        return dao.getDepositById(id)
    }

    suspend fun delete(deposit: DepositCalculation) {
        dao.delete(deposit)
    }
}