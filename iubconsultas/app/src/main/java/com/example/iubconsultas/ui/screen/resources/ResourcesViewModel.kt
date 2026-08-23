package com.example.iubconsultas.ui.screen.resources

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.catalog.ResourceResponse
import com.example.iubconsultas.data.remote.dto.catalog.resourceTypeFromLabel
import com.example.iubconsultas.data.repository.BlockRepository
import com.example.iubconsultas.data.repository.ResourceRepository
import kotlinx.coroutines.launch

class ResourcesViewModel : ViewModel() {

    private val resourceRepository = ResourceRepository(RetrofitClient.apiService)
    private val blockRepository = BlockRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(ResourcesUiState())
        private set

    init {
        loadResources()
        loadBlocks()
    }

    fun loadResources() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = resourceRepository.getResources()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, resources = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los recursos"
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

    fun loadBlocks() {
        viewModelScope.launch {
            try {
                val response = blockRepository.getBlocks()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(blocks = response.body()!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onNewResourceNameChange(value: String) {
        uiState = uiState.copy(newResourceName = value, error = null)
    }

    fun onBlockSelected(name: String) {
        uiState = uiState.copy(selectedBlockName = name, error = null)
    }

    fun onTypeSelected(label: String) {
        uiState = uiState.copy(selectedTypeLabel = label, error = null)
    }

    fun addResource() {
        val block = uiState.blocks.find { it.name == uiState.selectedBlockName }
        val type = resourceTypeFromLabel(uiState.selectedTypeLabel)

        if (uiState.newResourceName.isBlank() || block == null) {
            uiState = uiState.copy(error = "Completa el nombre y elige un bloque")
            return
        }

        viewModelScope.launch {
            try {
                val response = resourceRepository.createResource(
                    uiState.newResourceName,
                    type,
                    block.id
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(newResourceName = "", selectedBlockName = "")
                    loadResources()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear el recurso")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditResource(resource: ResourceResponse) {
        uiState = uiState.copy(
            editingResourceId = resource.id,
            editingResourceName = resource.name,
            error = null
        )
    }

    fun onEditResourceNameChange(value: String) {
        uiState = uiState.copy(editingResourceName = value, error = null)
    }

    fun cancelEditResource() {
        uiState = uiState.copy(editingResourceId = null, editingResourceName = "")
    }

    fun saveEditResource() {
        val id = uiState.editingResourceId ?: return

        if (uiState.editingResourceName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = resourceRepository.updateResource(id, uiState.editingResourceName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingResourceId = null, editingResourceName = "")
                    loadResources()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar el recurso")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteResource(id: Long) {
        viewModelScope.launch {
            try {
                val response = resourceRepository.deleteResource(id)

                if (response.isSuccessful) {
                    loadResources()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar el recurso")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}