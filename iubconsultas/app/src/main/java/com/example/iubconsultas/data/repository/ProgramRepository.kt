package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.catalog.CreateProgramRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateProgramRequest

class ProgramRepository(private val apiService: ApiService) {

    suspend fun getPrograms() = apiService.getPrograms()

    suspend fun createProgram(name: String) =
        apiService.createProgram(CreateProgramRequest(name))

    suspend fun updateProgram(id: Long, name: String) =
        apiService.updateProgram(id, UpdateProgramRequest(name))

    suspend fun deleteProgram(id: Long) =
        apiService.deleteProgram(id)
}