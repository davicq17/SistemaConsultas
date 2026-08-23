package com.example.iubconsultas.ui.screen.consultations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.local.SessionManager
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.data.remote.dto.comment.CommentResponse
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationResponse
import com.example.iubconsultas.data.remote.dto.consultation.consultationPriorityFromLabel
import com.example.iubconsultas.data.remote.dto.consultation.consultationStatusFromLabel
import com.example.iubconsultas.data.remote.dto.consultation.label
import com.example.iubconsultas.data.remote.dto.consultation.needsReason
import com.example.iubconsultas.data.remote.dto.consultation.allowsComments
import com.example.iubconsultas.data.remote.dto.consultation.nextStatesFor
import com.example.iubconsultas.data.remote.dto.consultation.toShortTime
import com.example.iubconsultas.data.remote.dto.user.displayName
import com.example.iubconsultas.data.repository.CommentRepository
import com.example.iubconsultas.data.repository.ConsultationRepository
import com.example.iubconsultas.data.repository.ModuleRepository
import com.example.iubconsultas.data.repository.ResourceRepository
import com.example.iubconsultas.data.repository.UserRepository
import kotlinx.coroutines.launch

class ConsultationsViewModel : ViewModel() {
    private val consultationRepository = ConsultationRepository(RetrofitClient.apiService)
    private val userRepository = UserRepository(RetrofitClient.apiService)
    private val moduleRepository = ModuleRepository(RetrofitClient.apiService)
    private val resourceRepository = ResourceRepository(RetrofitClient.apiService)
    private val commentRepository = CommentRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(ConsultationsUiState())
        private set

    init {
        loadConsultations()
        loadUsers()
        loadModules()
        loadResources()
    }

    fun loadConsultations() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = consultationRepository.getConsultations()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(isLoading = false, consultations = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar las solicitudes"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "No se pudo conectar al servidor"
                )
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val response = userRepository.getUsers()

                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!

                    uiState = uiState.copy(
                        students = users.filter { it.role == Role.STUDENT },
                        teachers = users.filter { it.role == Role.TEACHER }
                    )
                }
            } catch (e: Exception) {
            }
        }
    }

    fun loadModules() {
        viewModelScope.launch {
            try {
                val response = moduleRepository.getModules()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(modules = response.body()!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun loadResources() {
        viewModelScope.launch {
            try {
                val response = resourceRepository.getResources()

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(resources = response.body()!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun toggleFilters() {
        uiState = uiState.copy(showFilters = !uiState.showFilters)
    }

    fun onFilterDateChange(value: String) {
        uiState = uiState.copy(filterDate = value)
    }

    fun onFilterTimeChange(value: String) {
        uiState = uiState.copy(filterTime = value)
    }

    fun onFilterMonthSelected(value: String) {
        uiState = uiState.copy(filterMonth = value)
    }

    fun onFilterTeacherChange(value: String) {
        uiState = uiState.copy(filterTeacher = value)
    }

    fun onFilterStudentChange(value: String) {
        uiState = uiState.copy(filterStudent = value)
    }

    fun clearFilters() {
        uiState = uiState.copy(
            filterDate = "",
            filterTime = "",
            filterMonth = ALL_MONTHS,
            filterTeacher = "",
            filterStudent = ""
        )
    }

    fun applyFilters(consultations: List<ConsultationResponse>): List<ConsultationResponse> {
        val month = monthNumber(uiState.filterMonth)

        return consultations.filter { consultation ->

            val consultationMonth = if (consultation.consultationDate.length >= 7) {
                consultation.consultationDate.substring(5, 7)
            } else {
                ""
            }

            val teacher =
                "${consultation.teacherFullName} ${consultation.teacherIdentification}"
            val student =
                "${consultation.studentFullName} ${consultation.studentIdentification}"

            (month == null || consultationMonth == month) &&
                consultation.consultationDate.contains(uiState.filterDate, true) &&
                consultation.consultationTime.contains(uiState.filterTime, true) &&
                teacher.contains(uiState.filterTeacher, true) &&
                student.contains(uiState.filterStudent, true)
        }
    }

    fun openCreateDialog() {
        uiState = uiState.copy(showCreateDialog = true, error = null)
    }

    fun closeCreateDialog() {
        uiState = uiState.copy(showCreateDialog = false)
    }

    fun onSubjectChange(value: String) {
        uiState = uiState.copy(newSubject = value, error = null)
    }

    fun onDescriptionChange(value: String) {
        uiState = uiState.copy(newDescription = value, error = null)
    }

    fun onDateChange(value: String) {
        uiState = uiState.copy(newDate = value, error = null)
    }

    fun onTimeChange(value: String) {
        uiState = uiState.copy(newTime = value, error = null)
    }

    fun onPrioritySelected(label: String) {
        uiState = uiState.copy(newPriorityLabel = label, error = null)
    }

    fun onStudentSelected(name: String) {
        uiState = uiState.copy(selectedStudentName = name, error = null)
    }

    fun onTeacherSelected(name: String) {
        uiState = uiState.copy(selectedTeacherName = name, error = null)
    }

    fun onModuleSelected(name: String) {
        uiState = uiState.copy(selectedModuleName = name, error = null)
    }

    fun addConsultation() {
        val teacher = uiState.teachers.find { it.displayName() == uiState.selectedTeacherName }
        val module = uiState.modules.find { it.name == uiState.selectedModuleName }

        if (uiState.newSubject.isBlank() || uiState.newDescription.isBlank()) {
            uiState = uiState.copy(error = "Completa el asunto y la descripción")
            return
        }

        if (teacher == null || module == null) {
            uiState = uiState.copy(error = "Elige docente y módulo")
            return
        }

        if (!isValidDate(uiState.newDate)) {
            uiState = uiState.copy(error = "La fecha debe tener el formato AAAA-MM-DD")
            return
        }

        if (!isValidTime(uiState.newTime)) {
            uiState = uiState.copy(error = "La hora debe tener el formato HH:MM")
            return
        }

        viewModelScope.launch {
            try {
                val response = consultationRepository.createConsultation(
                    consultationPriorityFromLabel(uiState.newPriorityLabel),
                    uiState.newSubject,
                    uiState.newDescription,
                    uiState.newDate,
                    uiState.newTime,
                    teacher.id,
                    module.id
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        showCreateDialog = false,
                        newSubject = "",
                        newDescription = "",
                        newDate = "",
                        newTime = "",
                        newPriorityLabel = "Media",
                        selectedStudentName = "",
                        selectedTeacherName = "",
                        selectedModuleName = ""
                    )
                    loadConsultations()
                } else {
                    uiState = uiState.copy(error = "No se pudo crear la solicitud")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startEditConsultation(consultation: ConsultationResponse) {
        uiState = uiState.copy(
            editingConsultationId = consultation.id,
            editSubject = consultation.subject,
            editDescription = consultation.description,
            editDate = consultation.consultationDate,
            editTime = consultation.consultationTime.toShortTime(),
            editPriorityLabel = consultation.priority.label(),
            editModuleName = consultation.moduleName,
            editReason = "",
            editOriginalDate = consultation.consultationDate,
            editOriginalTime = consultation.consultationTime.toShortTime(),
            error = null
        )
    }

    fun onEditReasonChange(value: String) {
        uiState = uiState.copy(editReason = value, error = null)
    }

    fun onEditSubjectChange(value: String) {
        uiState = uiState.copy(editSubject = value, error = null)
    }

    fun onEditDescriptionChange(value: String) {
        uiState = uiState.copy(editDescription = value, error = null)
    }

    fun onEditDateChange(value: String) {
        uiState = uiState.copy(editDate = value, error = null)
    }

    fun onEditTimeChange(value: String) {
        uiState = uiState.copy(editTime = value, error = null)
    }

    fun onEditPrioritySelected(label: String) {
        uiState = uiState.copy(editPriorityLabel = label, error = null)
    }

    fun onEditModuleSelected(name: String) {
        uiState = uiState.copy(editModuleName = name, error = null)
    }

    fun cancelEditConsultation() {
        uiState = uiState.copy(editingConsultationId = null)
    }

    fun saveEditConsultation() {
        val id = uiState.editingConsultationId ?: return
        val module = uiState.modules.find { it.name == uiState.editModuleName }

        if (uiState.editSubject.isBlank() || uiState.editDescription.isBlank()) {
            uiState = uiState.copy(error = "Completa el asunto y la descripción")
            return
        }

        if (module == null) {
            uiState = uiState.copy(error = "Elige un módulo")
            return
        }

        if (!isValidDate(uiState.editDate)) {
            uiState = uiState.copy(error = "La fecha debe tener el formato AAAA-MM-DD")
            return
        }

        if (!isValidTime(uiState.editTime)) {
            uiState = uiState.copy(error = "La hora debe tener el formato HH:MM")
            return
        }

        val changedSchedule = uiState.editDate != uiState.editOriginalDate ||
            uiState.editTime != uiState.editOriginalTime

        if (changedSchedule && uiState.editReason.isBlank()) {
            uiState = uiState.copy(
                error = "Escribe el motivo del cambio de fecha u hora"
            )
            return
        }

        viewModelScope.launch {
            try {
                val response = consultationRepository.updateConsultation(
                    id,
                    consultationPriorityFromLabel(uiState.editPriorityLabel),
                    uiState.editSubject,
                    uiState.editDescription,
                    uiState.editDate,
                    uiState.editTime,
                    module.id,
                    if (changedSchedule) uiState.editReason else null
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingConsultationId = null)
                    loadConsultations()
                } else {
                    uiState = uiState.copy(error = "No se pudo actualizar la solicitud")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startAssignResource(consultation: ConsultationResponse) {
        uiState = uiState.copy(
            resourceConsultationId = consultation.id,
            selectedResourceName = "",
            error = null
        )
    }

    fun onResourceSelected(name: String) {
        uiState = uiState.copy(selectedResourceName = name)
    }

    fun cancelAssignResource() {
        uiState = uiState.copy(resourceConsultationId = null, selectedResourceName = "")
    }

    fun saveAssignResource() {
        val id = uiState.resourceConsultationId ?: return
        val resource = uiState.resources.find { it.name == uiState.selectedResourceName }

        if (resource == null) {
            uiState = uiState.copy(error = "Elige un recurso físico")
            return
        }

        viewModelScope.launch {
            try {
                val response = consultationRepository.assignResource(id, resource.id)

                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        resourceConsultationId = null,
                        selectedResourceName = ""
                    )
                    loadConsultations()
                } else {
                    uiState = uiState.copy(error = "No se pudo asignar el recurso")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startReassignTeacher(consultation: ConsultationResponse) {
        uiState = uiState.copy(
            teacherConsultationId = consultation.id,
            newTeacherNameForConsultation = "",
            error = null
        )
    }

    fun onNewTeacherSelected(name: String) {
        uiState = uiState.copy(newTeacherNameForConsultation = name)
    }

    fun cancelReassignTeacher() {
        uiState = uiState.copy(
            teacherConsultationId = null,
            newTeacherNameForConsultation = ""
        )
    }

    fun saveReassignTeacher() {
        val id = uiState.teacherConsultationId ?: return
        val teacher = uiState.teachers.find {
            it.displayName() == uiState.newTeacherNameForConsultation
        }

        if (teacher == null) {
            uiState = uiState.copy(error = "Elige un docente")
            return
        }

        viewModelScope.launch {
            try {
                val response = consultationRepository.reassignTeacher(id, teacher.id)

                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        teacherConsultationId = null,
                        newTeacherNameForConsultation = ""
                    )
                    loadConsultations()
                } else {
                    uiState = uiState.copy(error = "No se pudo reasignar el docente")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    fun startChangeStatus(consultation: ConsultationResponse) {
        val options = consultation.status
            .nextStatesFor(SessionManager.role)
            .map { it.label() }

        uiState = uiState.copy(
            statusConsultationId = consultation.id,
            statusOptions = options,
            statusLabel = options.firstOrNull() ?: "",
            statusReason = "",
            error = if (options.isEmpty()) {
                "Esta consulta está ${consultation.status.label().lowercase()} " +
                    "y ya no puede cambiar de estado"
            } else {
                null
            }
        )
    }

    fun openComments(consultation: ConsultationResponse) {
        uiState = uiState.copy(
            commentsConsultationId = consultation.id,
            commentsConsultationNumber = consultation.consultationNumber,
            commentsAllowed = consultation.status.allowsComments(),
            comments = emptyList(),
            newComment = "",
            commentsError = null
        )

        loadComments(consultation.id)
    }

    fun loadComments(consultationId: Long) {
        viewModelScope.launch {
            try {
                val response = commentRepository.getComments(consultationId)

                if (response.isSuccessful && response.body() != null) {
                    uiState = uiState.copy(comments = response.body()!!)
                } else {
                    uiState = uiState.copy(
                        commentsError = "No se pudieron cargar los comentarios"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(commentsError = "No se pudo conectar al servidor")
            }
        }
    }

    fun onNewCommentChange(value: String) {
        uiState = uiState.copy(newComment = value, commentsError = null)
    }

    fun closeComments() {
        uiState = uiState.copy(
            commentsConsultationId = null,
            newComment = "",
            editingCommentId = null,
            editingCommentText = ""
        )
    }

    fun canEditComment(comment: CommentResponse): Boolean =
        uiState.commentsAllowed && comment.authorId == SessionManager.userId

    fun startEditComment(comment: CommentResponse) {
        uiState = uiState.copy(
            editingCommentId = comment.id,
            editingCommentText = comment.content,
            commentsError = null
        )
    }

    fun onEditCommentChange(value: String) {
        uiState = uiState.copy(editingCommentText = value, commentsError = null)
    }

    fun cancelEditComment() {
        uiState = uiState.copy(editingCommentId = null, editingCommentText = "")
    }

    fun saveEditComment() {
        val commentId = uiState.editingCommentId ?: return
        val consultationId = uiState.commentsConsultationId ?: return

        if (uiState.editingCommentText.isBlank()) {
            uiState = uiState.copy(commentsError = "El comentario no puede quedar vacío")
            return
        }

        viewModelScope.launch {
            try {
                val response = commentRepository.updateComment(
                    commentId,
                    uiState.editingCommentText
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(editingCommentId = null, editingCommentText = "")
                    loadComments(consultationId)
                } else {
                    uiState = uiState.copy(commentsError = "No se pudo editar el comentario")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(commentsError = "No se pudo conectar al servidor")
            }
        }
    }

    fun addComment() {
        val id = uiState.commentsConsultationId ?: return

        if (uiState.newComment.isBlank()) {
            uiState = uiState.copy(commentsError = "Escribe un comentario")
            return
        }

        viewModelScope.launch {
            try {
                val response = commentRepository.createComment(uiState.newComment, id)

                if (response.isSuccessful) {
                    uiState = uiState.copy(newComment = "", commentsError = null)
                    loadComments(id)
                } else {
                    uiState = uiState.copy(commentsError = "No se pudo enviar el comentario")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(commentsError = "No se pudo conectar al servidor")
            }
        }
    }

    fun onStatusSelected(label: String) {
        uiState = uiState.copy(statusLabel = label, error = null)
    }

    fun onStatusReasonChange(value: String) {
        uiState = uiState.copy(statusReason = value, error = null)
    }

    fun cancelChangeStatus() {
        uiState = uiState.copy(
            statusConsultationId = null,
            statusLabel = "",
            statusReason = "",
            statusOptions = emptyList()
        )
    }

    fun saveChangeStatus() {
        val id = uiState.statusConsultationId ?: return

        if (uiState.statusOptions.isEmpty()) {
            return
        }

        val newStatus = consultationStatusFromLabel(uiState.statusLabel)

        if (newStatus.needsReason() && uiState.statusReason.isBlank()) {
            uiState = uiState.copy(
                error = "Escribe el motivo para ${uiState.statusLabel.lowercase()}"
            )
            return
        }

        viewModelScope.launch {
            try {
                val response = consultationRepository.changeStatus(
                    id,
                    newStatus,
                    if (newStatus.needsReason()) uiState.statusReason else null
                )

                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        statusConsultationId = null,
                        statusLabel = "",
                        statusReason = "",
                        statusOptions = emptyList()
                    )
                    loadConsultations()
                } else {
                    uiState = uiState.copy(error = "No se pudo cambiar el estado")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "No se pudo conectar al servidor")
            }
        }
    }

    private fun isValidDate(value: String): Boolean =
        Regex("\\d{4}-\\d{2}-\\d{2}").matches(value)

    private fun isValidTime(value: String): Boolean =
        Regex("\\d{2}:\\d{2}(:\\d{2})?").matches(value)
}
