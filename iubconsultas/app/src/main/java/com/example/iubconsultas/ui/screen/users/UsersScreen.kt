package com.example.iubconsultas.ui.screen.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.data.remote.dto.auth.label
import com.example.iubconsultas.data.remote.dto.user.UserResponse
import com.example.iubconsultas.ui.component.DropdownSelector
import com.example.iubconsultas.ui.component.InputField

@Composable
fun UsersContent(
    viewModel: UsersViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val visibleUsers = viewModel.filteredUsers()

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
                text = "${visibleUsers.size} de ${uiState.users.size} usuarios",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(onClick = { viewModel.toggleFilters() }) {
                Text(if (uiState.showFilters) "Ocultar filtros" else "Filtros")
            }
        }

        if (uiState.showFilters) {
            UserFilters(uiState = uiState, viewModel = viewModel)
        }

        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (visibleUsers.isEmpty()) {
            Text(
                text = "No hay usuarios para mostrar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(visibleUsers, key = { it.id }) { user ->
                    UserItem(
                        user = user,
                        onEditClick = { viewModel.startEditUser(user) },
                        onDeleteClick = { viewModel.deleteUser(user.id) }
                    )
                }
            }
        }
    }

    if (uiState.editingUserId != null) {
        EditUserDialog(uiState = uiState, viewModel = viewModel)
    }
}

@Composable
private fun UserFilters(
    uiState: UsersUiState,
    viewModel: UsersViewModel
) {
    Column {
        InputField(
            value = uiState.filterSearch,
            onValueChange = { viewModel.onFilterSearchChange(it) },
            label = "Buscar (nombre, correo o documento)"
        )

        DropdownSelector(
            label = "Rol",
            options = listOf(ALL_OPTION) + Role.entries.map { it.label() },
            selectedOption = uiState.filterRole,
            onOptionSelected = { viewModel.onFilterRoleSelected(it) }
        )

        DropdownSelector(
            label = "Estado",
            options = STATUS_OPTIONS,
            selectedOption = uiState.filterStatus,
            onOptionSelected = { viewModel.onFilterStatusSelected(it) }
        )

        TextButton(onClick = { viewModel.clearFilters() }) {
            Text("Limpiar filtros")
        }
    }
}

@Composable
private fun EditUserDialog(
    uiState: UsersUiState,
    viewModel: UsersViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.cancelEditUser() },
        title = { Text("Editar usuario") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                InputField(
                    value = uiState.editName,
                    onValueChange = { viewModel.onEditNameChange(it) },
                    label = "Nombre"
                )

                InputField(
                    value = uiState.editLastName,
                    onValueChange = { viewModel.onEditLastNameChange(it) },
                    label = "Apellido"
                )

                InputField(
                    value = uiState.editEmail,
                    onValueChange = { viewModel.onEditEmailChange(it) },
                    label = "Correo"
                )

                DropdownSelector(
                    label = "Programa",
                    options = listOf(NO_PROGRAM) + uiState.programs.map { it.name },
                    selectedOption = uiState.editProgramName,
                    onOptionSelected = { viewModel.onEditProgramSelected(it) }
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
            TextButton(onClick = { viewModel.saveEditUser() }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.cancelEditUser() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun UserItem(
    user: UserResponse,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val status = if (user.active) STATUS_ACTIVE else STATUS_INACTIVE

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
                    text = "${user.name} ${user.lastName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${user.role.label()} · ${user.identification} · $status",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (user.programName != null) {
                    Text(
                        text = user.programName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar"
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
        }
    }
}
