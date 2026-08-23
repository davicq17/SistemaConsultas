package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.catalog.CreateSedeRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateSedeRequest

class SedeRepository(private val apiService: ApiService) {

    suspend fun getSedes() = apiService.getSedes()

    suspend fun createSede(name: String) =
        apiService.createSede(CreateSedeRequest(name))

    suspend fun updateSede(id: Long, name: String) =
        apiService.updateSede(id, UpdateSedeRequest(name))

    suspend fun deleteSede(id: Long) =
        apiService.deleteSede(id)
}