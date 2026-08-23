package com.example.iubconsultas.data.remote.dto.consultation

import com.example.iubconsultas.data.remote.dto.auth.Role
import com.google.gson.annotations.SerializedName

enum class ConsultationStatus {
    @SerializedName("PENDIENTE")
    PENDING,

    @SerializedName("RECHAZADA")
    REJECTED,

    @SerializedName("EN_PROCESO")
    IN_PROGRESS,

    @SerializedName("RESUELTA")
    RESOLVED,

    @SerializedName("CANCELADA")
    CANCELLED
}

fun ConsultationStatus.label(): String = when (this) {
    ConsultationStatus.PENDING -> "Pendiente"
    ConsultationStatus.REJECTED -> "Rechazada"
    ConsultationStatus.IN_PROGRESS -> "En proceso"
    ConsultationStatus.RESOLVED -> "Resuelta"
    ConsultationStatus.CANCELLED -> "Cancelada"
}

fun consultationStatusFromLabel(label: String): ConsultationStatus =
    ConsultationStatus.entries.find { it.label() == label } ?: ConsultationStatus.PENDING

fun ConsultationStatus.nextStates(): List<ConsultationStatus> = when (this) {
    ConsultationStatus.PENDING -> listOf(
        ConsultationStatus.IN_PROGRESS,
        ConsultationStatus.REJECTED,
        ConsultationStatus.CANCELLED
    )

    ConsultationStatus.IN_PROGRESS -> listOf(
        ConsultationStatus.RESOLVED,
        ConsultationStatus.CANCELLED
    )

    ConsultationStatus.REJECTED,
    ConsultationStatus.CANCELLED,
    ConsultationStatus.RESOLVED -> emptyList()
}

fun ConsultationStatus.needsReason(): Boolean =
    this == ConsultationStatus.REJECTED || this == ConsultationStatus.CANCELLED

fun ConsultationStatus.nextStatesFor(role: Role?): List<ConsultationStatus> {
    val allowed = nextStates()

    return when (role) {
        Role.STUDENT -> allowed.filter { it == ConsultationStatus.CANCELLED }

        Role.TEACHER -> if (this == ConsultationStatus.PENDING) {
            allowed.filter { it != ConsultationStatus.CANCELLED }
        } else {
            allowed
        }

        else -> allowed
    }
}

fun ConsultationStatus.allowsComments(): Boolean =
    this == ConsultationStatus.IN_PROGRESS

fun String.toShortTime(): String =
    if (length >= 5) substring(0, 5) else this

enum class ConsultationPriority {
    @SerializedName("ALTA")
    HIGH,

    @SerializedName("MEDIA")
    MEDIUM,

    @SerializedName("BAJA")
    LOW
}

fun ConsultationPriority.label(): String = when (this) {
    ConsultationPriority.HIGH -> "Alta"
    ConsultationPriority.MEDIUM -> "Media"
    ConsultationPriority.LOW -> "Baja"
}

fun consultationPriorityFromLabel(label: String): ConsultationPriority =
    ConsultationPriority.entries.find { it.label() == label } ?: ConsultationPriority.MEDIUM

data class ConsultationResponse(
    val id: Long,

    @SerializedName("numeroConsulta")
    val consultationNumber: String,

    @SerializedName("estado")
    val status: ConsultationStatus,

    @SerializedName("prioridad")
    val priority: ConsultationPriority,

    @SerializedName("asunto")
    val subject: String,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("fechaCreacion")
    val createdAt: String,

    @SerializedName("fechaConsulta")
    val consultationDate: String,

    @SerializedName("horaConsulta")
    val consultationTime: String,

    @SerializedName("identificacionEstudiante")
    val studentIdentification: String,

    @SerializedName("nombreCompletoEstudiante")
    val studentFullName: String,

    @SerializedName("identificacionDocente")
    val teacherIdentification: String,

    @SerializedName("nombreCompletoDocente")
    val teacherFullName: String,

    @SerializedName("nombreModulo")
    val moduleName: String,

    @SerializedName("nombrePrograma")
    val programName: String?,

    @SerializedName("nombreRecursoFisico")
    val resourceName: String
)

data class CreateConsultationRequest(
    @SerializedName("prioridad")
    val priority: ConsultationPriority,

    @SerializedName("asunto")
    val subject: String,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("fechaConsulta")
    val consultationDate: String,

    @SerializedName("horaConsulta")
    val consultationTime: String,

    @SerializedName("docenteId")
    val teacherId: Long,

    @SerializedName("moduloId")
    val moduleId: Long
)

data class UpdateConsultationRequest(
    @SerializedName("prioridad")
    val priority: ConsultationPriority,

    @SerializedName("asunto")
    val subject: String,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("fechaConsulta")
    val consultationDate: String,

    @SerializedName("horaConsulta")
    val consultationTime: String,

    @SerializedName("moduloId")
    val moduleId: Long,

    @SerializedName("motivo")
    val reason: String?
)

data class ChangeStatusRequest(
    @SerializedName("estado")
    val status: ConsultationStatus,

    @SerializedName("motivo")
    val reason: String?
)

data class AssignResourceRequest(
    @SerializedName("recursoFisicoId")
    val resourceId: Long
)

data class ReassignTeacherRequest(
    @SerializedName("docenteId")
    val teacherId: Long
)
