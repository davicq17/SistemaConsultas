package com.example.iubconsultas.ui.screen.consultations

import com.example.iubconsultas.data.remote.dto.catalog.ModuleResponse
import com.example.iubconsultas.data.remote.dto.comment.CommentResponse
import com.example.iubconsultas.data.remote.dto.catalog.ResourceResponse
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationResponse
import com.example.iubconsultas.data.remote.dto.user.UserResponse

const val ALL_MONTHS = "Todos"

val MONTHS = listOf(
    ALL_MONTHS,
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

fun monthNumber(label: String): String? {
    val position = MONTHS.indexOf(label)

    if (position <= 0) {
        return null
    }

    return position.toString().padStart(2, '0')
}

data class ConsultationsUiState(
    val consultations: List<ConsultationResponse> = emptyList(),
    val students: List<UserResponse> = emptyList(),
    val teachers: List<UserResponse> = emptyList(),
    val modules: List<ModuleResponse> = emptyList(),
    val resources: List<ResourceResponse> = emptyList(),

    val showCreateDialog: Boolean = false,
    val newSubject: String = "",
    val newDescription: String = "",
    val newDate: String = "",
    val newTime: String = "",
    val newPriorityLabel: String = "Media",
    val selectedStudentName: String = "",
    val selectedTeacherName: String = "",
    val selectedModuleName: String = "",

    val editingConsultationId: Long? = null,
    val editSubject: String = "",
    val editDescription: String = "",
    val editDate: String = "",
    val editTime: String = "",
    val editPriorityLabel: String = "",
    val editModuleName: String = "",
    val editReason: String = "",

    val editOriginalDate: String = "",
    val editOriginalTime: String = "",

    val statusConsultationId: Long? = null,
    val statusLabel: String = "",
    val statusOptions: List<String> = emptyList(),
    val statusReason: String = "",

    val resourceConsultationId: Long? = null,
    val selectedResourceName: String = "",

    val teacherConsultationId: Long? = null,
    val newTeacherNameForConsultation: String = "",

    val commentsConsultationId: Long? = null,
    val commentsConsultationNumber: String = "",
    val commentsAllowed: Boolean = false,
    val comments: List<CommentResponse> = emptyList(),
    val newComment: String = "",
    val editingCommentId: Long? = null,
    val editingCommentText: String = "",
    val commentsError: String? = null,

    val showFilters: Boolean = false,
    val filterDate: String = "",
    val filterTime: String = "",
    val filterMonth: String = ALL_MONTHS,
    val filterTeacher: String = "",
    val filterStudent: String = "",

    val isLoading: Boolean = false,
    val error: String? = null
)
