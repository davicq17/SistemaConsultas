package com.example.iubconsultas.ui.screen.users

import com.example.iubconsultas.data.remote.dto.catalog.ProgramResponse
import com.example.iubconsultas.data.remote.dto.user.UserResponse

const val NO_PROGRAM = "Sin programa"

const val ALL_OPTION = "Todos"
const val STATUS_ACTIVE = "Activo"
const val STATUS_INACTIVE = "Inactivo"

val STATUS_OPTIONS = listOf(ALL_OPTION, STATUS_ACTIVE, STATUS_INACTIVE)

data class UsersUiState(
    val users: List<UserResponse> = emptyList(),
    val programs: List<ProgramResponse> = emptyList(),

    val editingUserId: Long? = null,
    val editName: String = "",
    val editLastName: String = "",
    val editEmail: String = "",
    val editProgramName: String = NO_PROGRAM,

    val showFilters: Boolean = false,
    val filterSearch: String = "",
    val filterRole: String = ALL_OPTION,
    val filterStatus: String = ALL_OPTION,

    val isLoading: Boolean = false,
    val error: String? = null
)
