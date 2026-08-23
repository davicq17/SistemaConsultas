package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.auth.LoginRequest
import com.example.iubconsultas.data.remote.dto.auth.RegisterRequest

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    suspend fun register(request: RegisterRequest) =
        apiService.register(request)
}
