package com.example.iubconsultas.ui.screen.programs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.catalog.ProgramResponse
import com.example.iubconsultas.data.repository.ProgramRepository
import kotlinx.coroutines.launch

class ProgramsViewModel : ViewModel() {

    private val programRepository = ProgramRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(ProgramsUiState())
        private set

    init {
        loadPrograms()
    }

    fun loadPrograms() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = programRepository.getPrograms()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, programs = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los programas"
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

    fun onNewProgramNameChange(value: String) {
        uiState = uiState.copy(newProgramName = value, error = null)
    }

    fun addProgram() {
        if (uiState.newProgramName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = programRepository.createProgram(uiState.newProgramName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(newProgramName = "")
                    loadPrograms()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear el programa")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditProgram(program: ProgramResponse) {
        uiState = uiState.copy(
            editingProgramId = program.id,
            editingProgramName = program.name,
            error = null
        )
    }

    fun onEditProgramNameChange(value: String) {
        uiState = uiState.copy(editingProgramName = value, error = null)
    }

    fun cancelEditProgram() {
        uiState = uiState.copy(editingProgramId = null, editingProgramName = "")
    }

    fun saveEditProgram() {
        val id = uiState.editingProgramId ?: return

        if (uiState.editingProgramName.isBlank()) {
            uiState = uiState.copy(error = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            try {
                val response = programRepository.updateProgram(id, uiState.editingProgramName)

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingProgramId = null, editingProgramName = "")
                    loadPrograms()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar el programa")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteProgram(id: Long) {
        viewModelScope.launch {
            try {
                val response = programRepository.deleteProgram(id)

                if (response.isSuccessful) {
                    loadPrograms()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar el programa")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}