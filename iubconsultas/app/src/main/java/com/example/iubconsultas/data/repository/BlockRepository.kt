package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.catalog.CreateBlockRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateBlockRequest

class BlockRepository(private val apiService: ApiService) {

    suspend fun getBlocks() = apiService.getBlocks()

    suspend fun createBlock(name: String, sedeId: Long) =
        apiService.createBlock(CreateBlockRequest(name, sedeId))

    suspend fun updateBlock(id: Long, name: String) =
        apiService.updateBlock(id, UpdateBlockRequest(name))

    suspend fun deleteBlock(id: Long) =
        apiService.deleteBlock(id)
}