package com.example.iubconsultas.ui.screen.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.catalog.BlockResponse
import com.example.iubconsultas.data.repository.BlockRepository
import com.example.iubconsultas.data.repository.SedeRepository
import kotlinx.coroutines.launch

class BlocksViewModel : ViewModel() {

    private val blockRepository = BlockRepository(RetrofitClient.apiService)
    private val sedeRepository = SedeRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(BlocksUiState())
        private set

    init {
        loadBlocks()
        loadSedes()
    }

    fun loadBlocks() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = blockRepository.getBlocks()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, blocks = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los bloques"
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

    fun loadSedes() {
        viewModelScope.launch {
            try {
                val response = sedeRepository.getSedes()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(sedes = response.body()!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onNewBlockNameChange(value: String) {
        uiState = uiState.copy(newBlockName = value, error = null)
    }

    fun onSedeSelected(name: String) {
        uiState = uiState.copy(selectedSedeName = name, error = null)
    }

    fun addBlock() {
        val sede = uiState.sedes.find { it.name == uiState.selectedSedeName }

        if (uiState.newBlockName.isBlank() || sede == null) {
            uiState = uiState.copy(error = "Completa el nombre y elige una sede")
            return
        }

        viewModelScope.launch {
            try {
                val response = blockRepository.createBlock(uiState.newBlockName, sede.id)

                if (response.isSuccessful) {
                    uiState = uiState.copy(newBlockName = "", selectedSedeName = "")
                    loadBlocks()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear el bloque")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditBlock(block: BlockResponse) {
        uiState = uiState.copy(
            editingBlockId = block.id,
            editingBlockName = block.name,
            error = null
        )
    }

    fun onEditBlockNameChange(value: String) {
        uiState = uiState.copy(editingBlockName = value, error = null)
    }

    fun cancelEditBlock() {
        uiState = uiState.copy(editingBlockId = null, editingBlockName = "")
    }

    fun saveEditBlock() {
        val id = uiState.editingBlockId ?: return

        if (uiState.editingBlockName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = blockRepository.updateBlock(id, uiState.editingBlockName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingBlockId = null, editingBlockName = "")
                    loadBlocks()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar el bloque")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteBlock(id: Long) {
        viewModelScope.launch {
            try {
                val response = blockRepository.deleteBlock(id)

                if (response.isSuccessful) {
                    loadBlocks()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar el bloque")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}