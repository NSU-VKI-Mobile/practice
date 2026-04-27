package ci.nsu.mobile.main.ViewModel
import ci.nsu.mobile.main.Data.Local.DepositDao
import ci.nsu.mobile.main.Data.Local.DepositEntity
import kotlinx.coroutines.flow.Flow


class DepositRepository(private val depositDao: DepositDao) {
    val allDeposits: Flow<List<DepositEntity>> = depositDao.getAllDeposits()

    suspend fun insert(deposit: DepositEntity) {
        depositDao.insertDeposit(deposit)
    }

    suspend fun getDepositById(id: Long): DepositEntity? {
        return depositDao.getDepositById(id)
    }

    fun calculateDeposit(
        initial: Double,
        months: Int,
        rate: Double,
        monthlyTopUp: Double
    ): Pair<Double, Double> {
        var total = initial
        val monthlyRate = rate / 12 / 100

        for (i in 1..months) {
            total += total * monthlyRate
            total += monthlyTopUp
        }

        val interestEarned = total - initial - (monthlyTopUp * months)
        return Pair(total, interestEarned)
    }
}