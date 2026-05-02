package ci.nsu.mobile.main

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositCalculation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DepositDaoInstrumentedTest {
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun calculationsAreFilteredAndDeletedByCurrentUser() = runBlocking {
        val dao = database.depositDao()
        dao.insertCalculation(testCalculation(id = 1, userId = 10, initialAmount = 1_000.0))
        dao.insertCalculation(testCalculation(id = 2, userId = 20, initialAmount = 2_000.0))

        val userTenCalculations = dao.getCalculationsForUser(
            userId = 10,
            minAmount = null,
            maxAmount = null,
            fromDate = null,
            toDate = null
        ).first()
        assertEquals(listOf(1L), userTenCalculations.map { it.id })

        val filteredOutByAmount = dao.getCalculationsForUser(
            userId = 10,
            minAmount = 1_500.0,
            maxAmount = null,
            fromDate = null,
            toDate = null
        ).first()
        assertEquals(emptyList<DepositCalculation>(), filteredOutByAmount)

        dao.deleteCalculationByIdForUser(id = 2, userId = 10)
        val otherUserStillHasCalculation = dao.getCalculationsForUser(
            userId = 20,
            minAmount = null,
            maxAmount = null,
            fromDate = null,
            toDate = null
        ).first()
        assertEquals(listOf(2L), otherUserStillHasCalculation.map { it.id })

        dao.deleteCalculationByIdForUser(id = 1, userId = 10)
        val userTenAfterDelete = dao.getCalculationsForUser(
            userId = 10,
            minAmount = null,
            maxAmount = null,
            fromDate = null,
            toDate = null
        ).first()
        assertEquals(emptyList<DepositCalculation>(), userTenAfterDelete)
    }

    private fun testCalculation(
        id: Long,
        userId: Long,
        initialAmount: Double
    ): DepositCalculation {
        return DepositCalculation(
            id = id,
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = 12,
            interestRate = 5.0,
            monthlyTopUp = 500.0,
            finalAmount = initialAmount + 6_500.0,
            interestEarned = 500.0,
            calculationDate = 1_700_000_000_000L + id
        )
    }
}
