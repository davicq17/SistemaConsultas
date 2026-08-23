package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.user.UpdateUserRequest

class UserRepository(private val apiService: ApiService) {

    suspend fun getUsers() = apiService.getUsers()

    suspend fun getUser(id: Long) = apiService.getUser(id)

    suspend fun updateUser(
        id: Long,
        name: String,
        lastName: String,
        email: String,
        programId: Long?
    ) = apiService.updateUser(id, UpdateUserRequest(name, lastName, email, programId))

    suspend fun deleteUser(id: Long) = apiService.deleteUser(id)
}
