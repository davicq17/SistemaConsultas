package com.example.iubconsultas.ui.screen.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.catalog.ResourceResponse
import com.example.iubconsultas.data.remote.dto.catalog.ResourceType
import com.example.iubconsultas.data.remote.dto.catalog.label
import com.example.iubconsultas.ui.component.DropdownSelector
import com.example.iubconsultas.ui.component.EditDialog
import com.example.iubconsultas.ui.component.InputField
import com.example.iubconsultas.ui.component.PrimaryButton

@Composable
fun ResourcesContent(
    viewModel: ResourcesViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        InputField(
            value = uiState.newResourceName,
            onValueChange = { viewModel.onNewResourceNameChange(it) },
            label = "Nombre del recurso",
            error = uiState.error
        )

        DropdownSelector(
            label = "Bloque",
            options = uiState.blocks.map { it.name },
            selectedOption = uiState.selectedBlockName,
            onOptionSelected = { viewModel.onBlockSelected(it) }
        )

        DropdownSelector(
            label = "Tipo",
            options = ResourceType.entries.map { it.label() },
            selectedOption = uiState.selectedTypeLabel,
            onOptionSelected = { viewModel.onTypeSelected(it) }
        )

        PrimaryButton(
            text = "Agregar",
            onClick = { viewModel.addResource() }
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.resources, key = { it.id }) { resource ->
                    ResourceItem(
                        resource = resource,
                        onEditClick = { viewModel.startEditResource(resource) },
                        onDeleteClick = { viewModel.deleteResource(resource.id) }
                    )
                }
            }
        }
    }

    if (uiState.editingResourceId != null) {
        EditDialog(
            title = "Editar recurso",
            name = uiState.editingResourceName,
            onNameChange = { viewModel.onEditResourceNameChange(it) },
            nameLabel = "Nombre del recurso",
            onConfirm = { viewModel.saveEditResource() },
            onDismiss = { viewModel.cancelEditResource() }
        )
    }
}

@Composable
private fun ResourceItem(
    resource: ResourceResponse,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                    text = resource.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${resource.blockName} · ${resource.type.label()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
