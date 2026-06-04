package ci.nsu.mobile.calculations.data.mapper

import ci.nsu.mobile.calculations.data.entity.DepositEntity
import ci.nsu.mobile.domain.model.DepositCalculation
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapperTest {

    private val fullEntity = DepositEntity(
        id = 1,
        userId = 10,
        startAmount = 50_000.0,
        months = 24,
        rate = 12.5,
        monthlyTopUp = 2_000.0,
        finalAmount = 100_000.0,
        profit = 26_000.0,
        date = 1_700_000_000_000L
    )

    private val fullDomain = DepositCalculation(
        id = 2,
        userId = 5,
        startAmount = 30_000.0,
        months = 6,
        rate = 8.0,
        monthlyTopUp = null,
        finalAmount = 45_000.0,
        profit = 15_000.0,
        date = 1_600_000_000_000L
    )

    @Test
    fun `when entity mapped to domain then id is preserved`() {
        assertEquals(fullEntity.id, fullEntity.toDomain().id)
    }

    @Test
    fun `when entity mapped to domain then userId is preserved`() {
        assertEquals(fullEntity.userId, fullEntity.toDomain().userId)
    }

    @Test
    fun `when entity mapped to domain then startAmount is preserved`() {
        assertEquals(fullEntity.startAmount, fullEntity.toDomain().startAmount)
    }

    @Test
    fun `when entity mapped to domain then months is preserved`() {
        assertEquals(fullEntity.months, fullEntity.toDomain().months)
    }

    @Test
    fun `when entity mapped to domain then rate is preserved`() {
        assertEquals(fullEntity.rate, fullEntity.toDomain().rate)
    }

    @Test
    fun `when entity mapped to domain then monthlyTopUp is preserved`() {
        assertEquals(fullEntity.monthlyTopUp, fullEntity.toDomain().monthlyTopUp)
    }

    @Test
    fun `when entity mapped to domain then finalAmount is preserved`() {
        assertEquals(fullEntity.finalAmount, fullEntity.toDomain().finalAmount)
    }

    @Test
    fun `when entity mapped to domain then profit is preserved`() {
        assertEquals(fullEntity.profit, fullEntity.toDomain().profit)
    }

    @Test
    fun `when entity mapped to domain then date is preserved`() {
        assertEquals(fullEntity.date, fullEntity.toDomain().date)
    }

    @Test
    fun `when entity with null monthlyTopUp is mapped then domain also has null`() {
        val entity = fullEntity.copy(monthlyTopUp = null)
        assertNull(entity.toDomain().monthlyTopUp)
    }

    @Test
    fun `when domain mapped to entity then id is preserved`() {
        assertEquals(fullDomain.id, fullDomain.toEntity().id)
    }

    @Test
    fun `when domain mapped to entity then userId is preserved`() {
        assertEquals(fullDomain.userId, fullDomain.toEntity().userId)
    }

    @Test
    fun `when domain mapped to entity then startAmount is preserved`() {
        assertEquals(fullDomain.startAmount, fullDomain.toEntity().startAmount)
    }

    @Test
    fun `when domain mapped to entity then months is preserved`() {
        assertEquals(fullDomain.months, fullDomain.toEntity().months)
    }

    @Test
    fun `when domain mapped to entity then rate is preserved`() {
        assertEquals(fullDomain.rate, fullDomain.toEntity().rate)
    }

    @Test
    fun `when domain mapped to entity then monthlyTopUp null is preserved`() {
        assertNull(fullDomain.toEntity().monthlyTopUp)
    }

    @Test
    fun `when domain mapped to entity then finalAmount is preserved`() {
        assertEquals(fullDomain.finalAmount, fullDomain.toEntity().finalAmount)
    }

    @Test
    fun `when domain mapped to entity then profit is preserved`() {
        assertEquals(fullDomain.profit, fullDomain.toEntity().profit)
    }

    @Test
    fun `when domain mapped to entity then date is preserved`() {
        assertEquals(fullDomain.date, fullDomain.toEntity().date)
    }

    @Test
    fun `when domain with non-null monthlyTopUp is mapped then value is preserved in entity`() {
        val domain = fullDomain.copy(monthlyTopUp = 750.0)
        assertEquals(750.0, domain.toEntity().monthlyTopUp)
    }

    @Test
    fun `when entity is round-tripped through domain then result equals original entity`() {
        val roundTripped = fullEntity.toDomain().toEntity()
        assertEquals(fullEntity, roundTripped)
    }

    @Test
    fun `when domain is round-tripped through entity then result equals original domain`() {
        val roundTripped = fullDomain.toEntity().toDomain()
        assertEquals(fullDomain, roundTripped)
    }

    @Test
    fun `when entity with null monthlyTopUp is round-tripped then null is preserved`() {
        val entity = fullEntity.copy(monthlyTopUp = null)
        val roundTripped = entity.toDomain().toEntity()
        assertNull(roundTripped.monthlyTopUp)
    }
}