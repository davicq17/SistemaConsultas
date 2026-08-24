package com.example.iubconsultas.ui.screen.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.auth.label
import com.example.iubconsultas.data.remote.dto.user.UserResponse
import com.example.iubconsultas.data.repository.ProgramRepository
import com.example.iubconsultas.data.repository.UserRepository
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val userRepository = UserRepository(RetrofitClient.apiService)
    private val programRepository = ProgramRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(UsersUiState())
        private set

    init {
        loadUsers()
        loadPrograms()
    }

    fun loadUsers() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = userRepository.getUsers()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, users = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los usuarios"
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

    fun loadPrograms() {
        viewModelScope.launch {
            try {
                val response = programRepository.getPrograms()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(programs = response.body()!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun toggleFilters() {
        uiState = uiState.copy(showFilters = !uiState.showFilters)
    }

    fun onFilterSearchChange(value: String) {
        uiState = uiState.copy(filterSearch = value)
    }

    fun onFilterRoleSelected(value: String) {
        uiState = uiState.copy(filterRole = value)
    }

    fun onFilterStatusSelected(value: String) {
        uiState = uiState.copy(filterStatus = value)
    }

    fun clearFilters() {
        uiState = uiState.copy(
            filterSearch = "",
            filterRole = ALL_OPTION,
            filterStatus = ALL_OPTION
        )
    }

    fun filteredUsers(): List<UserResponse> {
        return uiState.users.filter { user ->
            val text = "${user.name} ${user.lastName} ${user.email} ${user.identification}"

            val roleOk = uiState.filterRole == ALL_OPTION ||
                user.role.label() == uiState.filterRole

            val statusOk = when (uiState.filterStatus) {
                STATUS_ACTIVE -> user.active
                STATUS_INACTIVE -> !user.active
                else -> true
            }

            text.contains(uiState.filterSearch, true) && roleOk && statusOk
        }
    }

    fun startEditUser(user: UserResponse) {
        uiState = uiState.copy(
            editingUserId = user.id,
            editName = user.name,
            editLastName = user.lastName,
            editEmail = user.email,
            editProgramName = user.programName ?: NO_PROGRAM,
            error = null
        )
    }

    fun onEditNameChange(value: String) {
        uiState = uiState.copy(editName = value, error = null)
    }

    fun onEditLastNameChange(value: String) {
        uiState = uiState.copy(editLastName = value, error = null)
    }

    fun onEditEmailChange(value: String) {
        uiState = uiState.copy(editEmail = value, error = null)
    }

    fun onEditProgramSelected(name: String) {
        uiState = uiState.copy(editProgramName = name, error = null)
    }

    fun cancelEditUser() {
        uiState = uiState.copy(editingUserId = null)
    }

    fun saveEditUser() {
        val id = uiState.editingUserId ?: return

        if (uiState.editName.isBlank() || uiState.editLastName.isBlank()) {
            uiState = uiState.copy(error = "Completa el nombre y el apellido")
            return
        }

        if (!uiState.editEmail.contains("@")) {
            uiState = uiState.copy(error = "El correo no tiene un formato válido")
            return
        }

        val programId = uiState.programs
            .find { it.name == uiState.editProgramName }
            ?.id

        viewModelScope.launch {
            try {
                val response = userRepository.updateUser(
                    id,
                    uiState.editName,
                    uiState.editLastName,
                    uiState.editEmail,
                    programId
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingUserId = null)
                    loadUsers()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar el usuario")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            try {
                val response = userRepository.deleteUser(id)

                if (response.isSuccessful) {
                    loadUsers()
                } else {
                    uiState = uiState.copy(error = "No se pudo eliminar el usuario")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }
}
