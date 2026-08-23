package com.example.iubconsultas.ui.screen.programs

import com.example.iubconsultas.data.remote.dto.catalog.ProgramResponse

data class ProgramsUiState(
    val programs: List<ProgramResponse> = emptyList(),
    val newProgramName: String = "",
    val editingProgramId: Long? = null,
    val editingProgramName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
