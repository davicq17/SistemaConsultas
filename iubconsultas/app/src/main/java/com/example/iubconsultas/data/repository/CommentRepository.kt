package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.comment.CreateCommentRequest
import com.example.iubconsultas.data.remote.dto.comment.UpdateCommentRequest

class CommentRepository(private val apiService: ApiService) {

    suspend fun getComments(consultationId: Long) =
        apiService.getCommentsByConsultation(consultationId)

    suspend fun createComment(content: String, consultationId: Long) =
        apiService.createComment(CreateCommentRequest(content, consultationId))

    suspend fun updateComment(id: Long, content: String) =
        apiService.updateComment(id, UpdateCommentRequest(content))
}
