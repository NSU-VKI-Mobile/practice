package ci.nsu.mobile.main.features.main

import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.model.dto.UserDto

data class MainState(
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,

    val users: List<UserDto> = emptyList()
)