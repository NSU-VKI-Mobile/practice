package ci.nsu.mobile.main.core.ui

import ci.nsu.mobile.main.domain.Gender
import ci.nsu.mobile.main.model.dto.GroupDto
import java.time.LocalDate

sealed class FieldValue {
    data class Text(val value: String) : FieldValue()
    data class Date(val value: LocalDate?) : FieldValue()
    data class GenderValue(val value: Gender?) : FieldValue()
    data class Group(val value: GroupDto?) : FieldValue()
}