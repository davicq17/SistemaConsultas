package com.example.iubconsultas.data.repository

import com.example.iubconsultas.data.remote.ApiService
import com.example.iubconsultas.data.remote.dto.consultation.AssignResourceRequest
import com.example.iubconsultas.data.remote.dto.consultation.ChangeStatusRequest
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationPriority
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationStatus
import com.example.iubconsultas.data.remote.dto.consultation.CreateConsultationRequest
import com.example.iubconsultas.data.remote.dto.consultation.ReassignTeacherRequest
import com.example.iubconsultas.data.remote.dto.consultation.UpdateConsultationRequest

class ConsultationRepository(private val apiService: ApiService) {

    suspend fun getConsultations() = apiService.getConsultations()

    suspend fun createConsultation(
        priority: ConsultationPriority,
        subject: String,
        description: String,
        consultationDate: String,
        consultationTime: String,
        teacherId: Long,
        moduleId: Long
    ) = apiService.createConsultation(
        CreateConsultationRequest(
            priority,
            subject,
            description,
            consultationDate,
            consultationTime,
            teacherId,
            moduleId
        )
    )

    suspend fun updateConsultation(
        id: Long,
        priority: ConsultationPriority,
        subject: String,
        description: String,
        consultationDate: String,
        consultationTime: String,
        moduleId: Long,
        reason: String?
    ) = apiService.updateConsultation(
        id,
        UpdateConsultationRequest(
            priority,
            subject,
            description,
            consultationDate,
            consultationTime,
            moduleId,
            reason
        )
    )

    suspend fun changeStatus(id: Long, status: ConsultationStatus, reason: String?) =
        apiService.changeConsultationStatus(id, ChangeStatusRequest(status, reason))

    suspend fun assignResource(id: Long, resourceId: Long) =
        apiService.assignConsultationResource(id, AssignResourceRequest(resourceId))

    suspend fun reassignTeacher(id: Long, teacherId: Long) =
        apiService.reassignConsultationTeacher(id, ReassignTeacherRequest(teacherId))
}
