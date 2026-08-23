package com.example.iubconsultas.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.local.SessionManager
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun login(onSuccess: () -> Unit) {

        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(error = "Completa el correo y la contraseña")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = authRepository.login(uiState.email, uiState.password)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    SessionManager.saveSession(
                        token = body.token,
                        userId = body.id,
                        email = body.email,
                        name = body.name,
                        role = body.role
                    )
                    uiState = uiState.copy(isLoading = false)
                    onSuccess()
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "Correo o contraseña incorrectos"
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