package com.example.iubconsultas.ui.screen.sedes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.catalog.SedeResponse
import com.example.iubconsultas.data.repository.SedeRepository
import kotlinx.coroutines.launch

class SedesViewModel : ViewModel() {

    private val sedeRepository = SedeRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(SedesUiState())
        private set

    init {
        loadSedes()
    }

    fun loadSedes() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = sedeRepository.getSedes()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, sedes = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar las sedes"
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

    fun onNewSedeNameChange(value: String) {
        uiState = uiState.copy(newSedeName = value, error = null)
    }

    fun addSede() {
        if (uiState.newSedeName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = sedeRepository.createSede(uiState.newSedeName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(newSedeName = "")
                    loadSedes()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear la sede")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditSede(sede: SedeResponse) {
        uiState = uiState.copy(
            editingSedeId = sede.id,
            editingSedeName = sede.name,
            error = null
        )
    }

    fun onEditSedeNameChange(value: String) {
        uiState = uiState.copy(editingSedeName = value, error = null)
    }

    fun cancelEditSede() {
        uiState = uiState.copy(editingSedeId = null, editingSedeName = "")
    }

    fun saveEditSede() {
        val id = uiState.editingSedeId ?: return

        if (uiState.editingSedeName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = sedeRepository.updateSede(id, uiState.editingSedeName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingSedeId = null, editingSedeName = "")
                    loadSedes()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar la sede")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteSede(id: Long) {
        viewModelScope.launch {
            try {
                val response = sedeRepository.deleteSede(id)

                if (response.isSuccessful) {
                    loadSedes()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar la sede")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}