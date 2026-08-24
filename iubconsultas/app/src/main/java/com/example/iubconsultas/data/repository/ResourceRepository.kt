package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.catalog.CreateResourceRequest
import com.example.iubconsultas.data.remote.dto.catalog.ResourceType
import com.example.iubconsultas.data.remote.dto.catalog.UpdateResourceRequest

class ResourceRepository(private val apiService: ApiService) {

    suspend fun getResources() = apiService.getResources()

    suspend fun createResource(name: String, type: ResourceType, blockId: Long) =
        apiService.createResource(CreateResourceRequest(name, type, blockId))

    suspend fun updateResource(id: Long, name: String) =
        apiService.updateResource(id, UpdateResourceRequest(name))

    suspend fun deleteResource(id: Long) =
        apiService.deleteResource(id)
}