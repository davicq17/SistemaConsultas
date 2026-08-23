package com.example.iubconsultas.ui.screen.modules

import com.example.iubconsultas.data.remote.dto.catalog.ModuleResponse

data class ModulesUiState(
    val modules: List<ModuleResponse> = emptyList(),
    val newModuleName: String = "",
    val newModuleDescription: String = "",
    val editingModuleId: Long? = null,
    val editingModuleName: String = "",
    val editingModuleDescription: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
