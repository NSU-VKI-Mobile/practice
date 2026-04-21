package data

import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val depositDao: DepositDao
)
{
    //сохр. расчет
    suspend fun saveCalculation(deposit: DepositEntity){
        depositDao.insertDeposit(deposit)
    }

    //получ. списка всех расчетов
    fun getAllCalculations(): Flow<List<DepositEntity>> {
        return depositDao.getAllDeposit()
    }

    //получение одного расчета по ид
    suspend fun getCalculationById(id: Long): DepositEntity? {
        return depositDao.getDepositById(id)
    }

}