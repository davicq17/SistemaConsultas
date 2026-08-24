package com.example.iubconsultas.ui.screen.resources

import com.example.iubconsultas.data.remote.dto.catalog.BlockResponse
import com.example.iubconsultas.data.remote.dto.catalog.ResourceResponse

data class ResourcesUiState(
    val resources: List<ResourceResponse> = emptyList(),
    val blocks: List<BlockResponse> = emptyList(),
    val newResourceName: String = "",
    val selectedBlockName: String = "",
    val selectedTypeLabel: String = "Salón",
    val editingResourceId: Long? = null,
    val editingResourceName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
