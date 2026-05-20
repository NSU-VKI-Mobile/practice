package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.DepositCalculation

class FakeDepositRepository {

    private val history = mutableListOf(
        DepositCalculation(
            1,
            "20.05.2026 12:00",
            100000.0,
            12,
            5,
            5000.0,
            165000.0,
            15000.0
        )
    )

    fun getAll(): List<DepositCalculation> {
        return history
    }

    fun save(item: DepositCalculation) {
        history.add(item)
    }
}