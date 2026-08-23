package com.example.iubconsultas.ui.screen.register

import com.example.iubconsultas.data.remote.dto.auth.Role

data class RegisterUiState(
    val identification: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val role: Role = Role.STUDENT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
