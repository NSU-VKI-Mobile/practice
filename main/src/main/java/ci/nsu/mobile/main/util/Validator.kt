package ci.nsu.mobile.main.util

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}

object Validator {

    fun validateInitialAmount(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult.Invalid("Введите стартовый взнос")
        val amount = value.toDoubleOrNull()
            ?: return ValidationResult.Invalid("Введите корректное число")
        if (amount <= 0) return ValidationResult.Invalid("Сумма должна быть больше нуля")
        return ValidationResult.Valid
    }

    fun validatePeriodMonths(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult.Invalid("Введите срок вклада")
        val months = value.toIntOrNull()
            ?: return ValidationResult.Invalid("Введите целое число месяцев")
        if (months <= 0) return ValidationResult.Invalid("Срок должен быть больше нуля")
        return ValidationResult.Valid
    }

    fun validateMonthlyTopUp(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult.Valid
        val amount = value.toDoubleOrNull()
            ?: return ValidationResult.Invalid("Введите корректное число")
        if (amount < 0) return ValidationResult.Invalid("Пополнение не может быть отрицательным")
        return ValidationResult.Valid
    }
}
