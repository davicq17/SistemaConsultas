package com.example.iubconsultas.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.auth.RegisterRequest
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onIdentificationChange(value: String) {
        uiState = uiState.copy(identification = value, error = null)
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, error = null)
    }

    fun onLastNameChange(value: String) {
        uiState = uiState.copy(lastName = value, error = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun onRoleChange(value: Role) {
        uiState = uiState.copy(role = value, error = null)
    }

    fun register(onSuccess: () -> Unit) {

        if (uiState.identification.isBlank() ||
            uiState.name.isBlank() ||
            uiState.lastName.isBlank() ||
            uiState.email.isBlank() ||
            uiState.password.isBlank()
        ) {
            uiState = uiState.copy(error = "Completa todos los campos")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = authRepository.register(
                    RegisterRequest(
                        identification = uiState.identification,
                        name = uiState.name,
                        lastName = uiState.lastName,
                        email = uiState.email,
                        password = uiState.password,
                        role = uiState.role
                    )
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(isLoading = false, success = true)
                    onSuccess()
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudo registrar el usuario"
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
}
