package com.example.iubconsultas.ui.screen.sedes

import com.example.iubconsultas.data.remote.dto.catalog.SedeResponse

data class SedesUiState(
    val sedes: List<SedeResponse> = emptyList(),
    val newSedeName: String = "",
    val editingSedeId: Long? = null,
    val editingSedeName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
