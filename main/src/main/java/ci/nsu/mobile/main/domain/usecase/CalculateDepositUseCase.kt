package ci.nsu.mobile.main.domain.usecase

class CalculateDepositUseCase {

    fun execute(a: Double, m: Int, r: Double, t: Double?): Pair<Double, Double> {
        var total = a
        val monthlyRate = r / 100 / 12

        repeat(m) {
            total += t ?: 0.0
            total += total * monthlyRate
        }

        val interest = total - a - (t ?: 0.0) * m
        return Pair(total, interest)
    }
}
