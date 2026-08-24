package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.catalog.CreateModuleRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateModuleRequest

class ModuleRepository(private val apiService: ApiService) {

    suspend fun getModules() = apiService.getModules()

    suspend fun createModule(name: String, description: String) =
        apiService.createModule(CreateModuleRequest(name, description))

    suspend fun updateModule(id: Long, name: String, description: String) =
        apiService.updateModule(id, UpdateModuleRequest(name, description))

    suspend fun deleteModule(id: Long) =
        apiService.deleteModule(id)
}