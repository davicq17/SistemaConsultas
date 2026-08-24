package com.example.iubconsultas.ui.screen.blocks

import com.example.iubconsultas.data.remote.dto.catalog.BlockResponse
import com.example.iubconsultas.data.remote.dto.catalog.SedeResponse

data class BlocksUiState(
    val blocks: List<BlockResponse> = emptyList(),
    val sedes: List<SedeResponse> = emptyList(),
    val newBlockName: String = "",
    val selectedSedeName: String = "",
    val editingBlockId: Long? = null,
    val editingBlockName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
