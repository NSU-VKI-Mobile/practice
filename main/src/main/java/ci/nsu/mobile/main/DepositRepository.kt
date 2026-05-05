package ci.nsu.mobile.main

import kotlinx.coroutines.flow.Flow

// класс-посредник между ViewModel и базой данных
// Зачем нужен: Если позже понадобится брать данные из интернета — меняем только Repository.
class DepositRepository(private val dao: DepositDao) { //принимает DAO при создании и сохраняет в приватное поле

    suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return dao.insert(calculation) //вызывает insert из DAO
    }

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return dao.getAllCalculations()
    } //Возвращает поток всех расчётов (автоматически обновляется при изменении БД)

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id) //Возвращает один расчёт по ID или null
    }

    suspend fun deleteCalculation(calculation: DepositCalculation) {  //удаление
        dao.delete(calculation)
    }
}