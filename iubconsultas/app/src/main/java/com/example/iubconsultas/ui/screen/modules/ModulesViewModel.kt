package com.example.iubconsultas.ui.screen.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.catalog.ModuleResponse
import com.example.iubconsultas.data.repository.ModuleRepository
import kotlinx.coroutines.launch

class ModulesViewModel : ViewModel() {

    private val moduleRepository = ModuleRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(ModulesUiState())
        private set

    init {
        loadModules()
    }

    fun loadModules() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = moduleRepository.getModules()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, modules = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los módulos"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "No se pudo conectar al servidor"
                )
            }
        }
    }

    fun onNewModuleNameChange(value: String) {
        uiState = uiState.copy(newModuleName = value, error = null)
    }

    fun onNewModuleDescriptionChange(value: String) {
        uiState = uiState.copy(newModuleDescription = value, error = null)
    }

    fun addModule() {
        if (uiState.newModuleName.isBlank() || uiState.newModuleDescription.isBlank()) {
            uiState = uiState.copy(error = "Completa nombre y descripción")
            return
        }

        viewModelScope.launch {
            try {
                val response = moduleRepository.createModule(
                    uiState.newModuleName,
                    uiState.newModuleDescription
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(newModuleName = "", newModuleDescription = "")
                    loadModules()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear el módulo")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditModule(module: ModuleResponse) {
        uiState = uiState.copy(
            editingModuleId = module.id,
            editingModuleName = module.name,
            editingModuleDescription = module.description,
            error = null
        )
    }

    fun onEditModuleNameChange(value: String) {
        uiState = uiState.copy(editingModuleName = value, error = null)
    }

    fun onEditModuleDescriptionChange(value: String) {
        uiState = uiState.copy(editingModuleDescription = value, error = null)
    }

    fun cancelEditModule() {
        uiState = uiState.copy(
            editingModuleId = null,
            editingModuleName = "",
            editingModuleDescription = ""
        )
    }

    fun saveEditModule() {
        val id = uiState.editingModuleId ?: return

        if (uiState.editingModuleName.isBlank() || uiState.editingModuleDescription.isBlank()) {
            uiState = uiState.copy(error = "Completa nombre y descripción")
            return
        }

        viewModelScope.launch {
            try {
                val response = moduleRepository.updateModule(
                    id,
                    uiState.editingModuleName,
                    uiState.editingModuleDescription
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        editingModuleId = null,
                        editingModuleName = "",
                        editingModuleDescription = ""
                    )
                    loadModules()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar el módulo")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteModule(id: Long) {
        viewModelScope.launch {
            try {
                val response = moduleRepository.deleteModule(id)

                if (response.isSuccessful) {
                    loadModules()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar el módulo")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}