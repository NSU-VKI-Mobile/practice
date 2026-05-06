package data

import kotlinx.coroutines.flow.Flow

class DepositRepository( //посредник между dao и viewModel
    private val depositDao: DepositDao
)
{
    //сохр. расчет
    suspend fun saveCalculation(deposit: DepositCalculations){
        depositDao.insertDeposit(deposit)
    }

    //получ. списка всех расчетов
    fun getAllCalculations(): Flow<List<DepositCalculations>> {
        return depositDao.getAllDeposit()
    }

    //получение одного расчета по ид
    suspend fun getCalculationById(id: Long): DepositCalculations? {
        return depositDao.getDepositById(id)
    }

}