package com.example.iubconsultas.ui.screen.consultations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.data.remote.dto.comment.CommentResponse
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationPriority
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationResponse
import com.example.iubconsultas.data.remote.dto.consultation.label
import com.example.iubconsultas.data.remote.dto.consultation.toShortTime
import com.example.iubconsultas.data.remote.dto.user.displayName
import com.example.iubconsultas.ui.component.DropdownSelector
import com.example.iubconsultas.ui.component.InputField
import com.example.iubconsultas.ui.component.PrimaryButton
import com.example.iubconsultas.ui.component.SelectDialog

@Composable
fun ConsultationsListContent(
    consultations: List<ConsultationResponse>,
    role: Role?,
    viewModel: ConsultationsViewModel,
    showTeacherFilter: Boolean = true,
    showStudentFilter: Boolean = true
) {
    val uiState = viewModel.uiState
    val visibleConsultations = viewModel.applyFilters(consultations)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${visibleConsultations.size} de ${consultations.size} consultas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(onClick = { viewModel.toggleFilters() }) {
                Text(if (uiState.showFilters) "Ocultar filtros" else "Filtros")
            }
        }

        if (uiState.showFilters) {
            ConsultationFilters(
                uiState = uiState,
                viewModel = viewModel,
                showTeacherFilter = showTeacherFilter,
                showStudentFilter = showStudentFilter
            )
        }

        if (uiState.error != null && uiState.editingConsultationId == null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (visibleConsultations.isEmpty()) {
            Text(
                text = "No hay consultas para mostrar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(visibleConsultations, key = { it.id }) { consultation ->
                    ConsultationItem(
                        consultation = consultation,
                        role = role,
                        onEditClick = { viewModel.startEditConsultation(consultation) },
                        onChangeStatusClick = { viewModel.startChangeStatus(consultation) },
                        onAssignResourceClick = { viewModel.startAssignResource(consultation) },
                        onReassignTeacherClick = { viewModel.startReassignTeacher(consultation) },
                        onCommentsClick = { viewModel.openComments(consultation) }
                    )
                }
            }
        }
    }

    if (uiState.commentsConsultationId != null) {
        CommentsDialog(uiState = uiState, viewModel = viewModel)
    }

    if (uiState.editingConsultationId != null) {
        EditConsultationDialog(uiState = uiState, viewModel = viewModel)
    }

    if (uiState.statusConsultationId != null) {
        SelectDialog(
            title = if (role == Role.STUDENT) "Cancelar solicitud" else "Cambiar estado",
            label = "Estado",
            options = uiState.statusOptions,
            selectedOption = uiState.statusLabel,
            onOptionSelected = { viewModel.onStatusSelected(it) },
            onConfirm = { viewModel.saveChangeStatus() },
            onDismiss = { viewModel.cancelChangeStatus() },
            reason = uiState.statusReason,
            onReasonChange = { viewModel.onStatusReasonChange(it) },
            reasonLabel = "Motivo (obligatorio si rechazas o cancelas)",
            error = uiState.error
        )
    }

    if (uiState.resourceConsultationId != null) {
        SelectDialog(
            title = "Asignar recurso físico",
            label = "Recurso",
            options = uiState.resources.map { it.name },
            selectedOption = uiState.selectedResourceName,
            onOptionSelected = { viewModel.onResourceSelected(it) },
            onConfirm = { viewModel.saveAssignResource() },
            onDismiss = { viewModel.cancelAssignResource() }
        )
    }

    if (uiState.teacherConsultationId != null) {
        SelectDialog(
            title = "Reasignar docente",
            label = "Docente",
            options = uiState.teachers.map { it.displayName() },
            selectedOption = uiState.newTeacherNameForConsultation,
            onOptionSelected = { viewModel.onNewTeacherSelected(it) },
            onConfirm = { viewModel.saveReassignTeacher() },
            onDismiss = { viewModel.cancelReassignTeacher() }
        )
    }
}

@Composable
private fun CommentsDialog(
    uiState: ConsultationsUiState,
    viewModel: ConsultationsViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.closeComments() },
        title = { Text("Comentarios · ${uiState.commentsConsultationNumber}") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (uiState.comments.isEmpty()) {
                    Text(
                        text = "Todavía no hay comentarios",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    uiState.comments.forEach { comment ->
                        CommentItem(
                            comment = comment,
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }

                if (uiState.commentsAllowed) {
                    InputField(
                        value = uiState.newComment,
                        onValueChange = { viewModel.onNewCommentChange(it) },
                        label = "Escribe un comentario"
                    )

                    PrimaryButton(
                        text = "Enviar",
                        onClick = { viewModel.addComment() }
                    )
                } else {
                    Text(
                        text = "Solo se puede comentar mientras la consulta " +
                            "está en proceso.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (uiState.commentsError != null) {
                    Text(
                        text = uiState.commentsError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.closeComments() }) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun CommentItem(
    comment: CommentResponse,
    uiState: ConsultationsUiState,
    viewModel: ConsultationsViewModel
) {
    val isEditing = uiState.editingCommentId == comment.id

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = comment.authorName + if (comment.edited) " (editado)" else "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        if (isEditing) {
            InputField(
                value = uiState.editingCommentText,
                onValueChange = { viewModel.onEditCommentChange(it) },
                label = "Editar comentario"
            )

            Row(horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { viewModel.cancelEditComment() }) {
                    Text("Cancelar")
                }
                TextButton(onClick = { viewModel.saveEditComment() }) {
                    Text("Guardar")
                }
            }
        } else {
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )

            if (viewModel.canEditComment(comment)) {
                TextButton(
                    onClick = { viewModel.startEditComment(comment) },
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
                ) {
                    Text("Editar", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun ConsultationFilters(
    uiState: ConsultationsUiState,
    viewModel: ConsultationsViewModel,
    showTeacherFilter: Boolean,
    showStudentFilter: Boolean
) {
    Column {
        InputField(
            value = uiState.filterDate,
            onValueChange = { viewModel.onFilterDateChange(it) },
            label = "Fecha"
        )

        InputField(
            value = uiState.filterTime,
            onValueChange = { viewModel.onFilterTimeChange(it) },
            label = "Hora"
        )

        DropdownSelector(
            label = "Mes",
            options = MONTHS,
            selectedOption = uiState.filterMonth,
            onOptionSelected = { viewModel.onFilterMonthSelected(it) }
        )

        if (showTeacherFilter) {
            InputField(
                value = uiState.filterTeacher,
                onValueChange = { viewModel.onFilterTeacherChange(it) },
                label = "Docente (nombre o documento)"
            )
        }

        if (showStudentFilter) {
            InputField(
                value = uiState.filterStudent,
                onValueChange = { viewModel.onFilterStudentChange(it) },
                label = "Estudiante (nombre o documento)"
            )
        }

        TextButton(onClick = { viewModel.clearFilters() }) {
            Text("Limpiar filtros")
        }
    }
}

@Composable
fun ConsultationFormContent(
    viewModel: ConsultationsViewModel
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        InputField(
            value = uiState.newSubject,
            onValueChange = { viewModel.onSubjectChange(it) },
            label = "Asunto"
        )

        InputField(
            value = uiState.newDescription,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = "Descripción"
        )

        DropdownSelector(
            label = "Prioridad",
            options = ConsultationPriority.entries.map { it.label() },
            selectedOption = uiState.newPriorityLabel,
            onOptionSelected = { viewModel.onPrioritySelected(it) }
        )

        DropdownSelector(
            label = "Docente",
            options = uiState.teachers.map { it.displayName() },
            selectedOption = uiState.selectedTeacherName,
            onOptionSelected = { viewModel.onTeacherSelected(it) }
        )

        DropdownSelector(
            label = "Módulo",
            options = uiState.modules.map { it.name },
            selectedOption = uiState.selectedModuleName,
            onOptionSelected = { viewModel.onModuleSelected(it) }
        )

        InputField(
            value = uiState.newDate,
            onValueChange = { viewModel.onDateChange(it) },
            label = "Fecha (AAAA-MM-DD)"
        )

        InputField(
            value = uiState.newTime,
            onValueChange = { viewModel.onTimeChange(it) },
            label = "Hora (HH:MM)"
        )

        PrimaryButton(
            text = "Registrar consulta",
            onClick = { viewModel.addConsultation() }
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun EditConsultationDialog(
    uiState: ConsultationsUiState,
    viewModel: ConsultationsViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.cancelEditConsultation() },
        title = { Text("Editar solicitud") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                InputField(
                    value = uiState.editSubject,
                    onValueChange = { viewModel.onEditSubjectChange(it) },
                    label = "Asunto"
                )

                InputField(
                    value = uiState.editDescription,
                    onValueChange = { viewModel.onEditDescriptionChange(it) },
                    label = "Descripción"
                )

                DropdownSelector(
                    label = "Prioridad",
                    options = ConsultationPriority.entries.map { it.label() },
                    selectedOption = uiState.editPriorityLabel,
                    onOptionSelected = { viewModel.onEditPrioritySelected(it) }
                )

                DropdownSelector(
                    label = "Módulo",
                    options = uiState.modules.map { it.name },
                    selectedOption = uiState.editModuleName,
                    onOptionSelected = { viewModel.onEditModuleSelected(it) }
                )

                InputField(
                    value = uiState.editDate,
                    onValueChange = { viewModel.onEditDateChange(it) },
                    label = "Fecha (AAAA-MM-DD)"
                )

                InputField(
                    value = uiState.editTime,
                    onValueChange = { viewModel.onEditTimeChange(it) },
                    label = "Hora (HH:MM)"
                )

                InputField(
                    value = uiState.editReason,
                    onValueChange = { viewModel.onEditReasonChange(it) },
                    label = "Motivo (si cambias fecha u hora)"
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.saveEditConsultation() }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.cancelEditConsultation() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun ConsultationItem(
    consultation: ConsultationResponse,
    role: Role?,
    onEditClick: () -> Unit,
    onChangeStatusClick: () -> Unit,
    onAssignResourceClick: () -> Unit,
    onReassignTeacherClick: () -> Unit,
    onCommentsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = consultation.subject,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${consultation.consultationNumber} · ${consultation.status.label()} · ${consultation.priority.label()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Estudiante: ${consultation.studentFullName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Docente: ${consultation.teacherFullName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${consultation.moduleName} · ${consultation.consultationDate} ${consultation.consultationTime.toShortTime()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Recurso: ${consultation.resourceName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ConsultationMenu(
                role = role,
                onEditClick = onEditClick,
                onChangeStatusClick = onChangeStatusClick,
                onAssignResourceClick = onAssignResourceClick,
                onReassignTeacherClick = onReassignTeacherClick,
                onCommentsClick = onCommentsClick
            )
        }
    }
}

@Composable
private fun ConsultationMenu(
    role: Role?,
    onEditClick: () -> Unit,
    onChangeStatusClick: () -> Unit,
    onAssignResourceClick: () -> Unit,
    onReassignTeacherClick: () -> Unit,
    onCommentsClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val isStudent = role == Role.STUDENT
    val isTeacher = role == Role.TEACHER
    val isAdmin = role == Role.ADMINISTRATOR

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Acciones"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isStudent || isTeacher) {
                DropdownMenuItem(
                    text = { Text(if (isStudent) "Editar" else "Editar / Reprogramar") },
                    onClick = {
                        onEditClick()
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(if (isStudent) "Cancelar solicitud" else "Cambiar estado")
                    },
                    onClick = {
                        onChangeStatusClick()
                        expanded = false
                    }
                )
            }

            if (isTeacher || isAdmin) {
                DropdownMenuItem(
                    text = { Text("Asignar recurso") },
                    onClick = {
                        onAssignResourceClick()
                        expanded = false
                    }
                )
            }

            if (isStudent || isTeacher) {
                DropdownMenuItem(
                    text = { Text("Comentarios") },
                    onClick = {
                        onCommentsClick()
                        expanded = false
                    }
                )
            }

            if (isTeacher || isAdmin) {
                DropdownMenuItem(
                    text = { Text("Reasignar docente") },
                    onClick = {
                        onReassignTeacherClick()
                        expanded = false
                    }
                )
            }
        }
    }
}
