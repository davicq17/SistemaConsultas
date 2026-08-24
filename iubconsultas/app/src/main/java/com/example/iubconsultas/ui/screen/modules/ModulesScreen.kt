package com.example.iubconsultas.ui.screen.modules

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
import com.example.iubconsultas.data.remote.dto.catalog.ModuleResponse
import com.example.iubconsultas.ui.component.EditDialog
import com.example.iubconsultas.ui.component.InputField
import com.example.iubconsultas.ui.component.PrimaryButton

@Composable
fun ModulesContent(
    viewModel: ModulesViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        InputField(
            value = uiState.newModuleName,
            onValueChange = { viewModel.onNewModuleNameChange(it) },
            label = "Nombre del módulo",
            error = uiState.error
        )

        InputField(
            value = uiState.newModuleDescription,
            onValueChange = { viewModel.onNewModuleDescriptionChange(it) },
            label = "Descripción",
            error = uiState.error
        )

        PrimaryButton(
            text = "Agregar",
            onClick = { viewModel.addModule() }
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
                items(uiState.modules, key = { it.id }) { module ->
                    ModuleItem(
                        module = module,
                        onEditClick = { viewModel.startEditModule(module) },
                        onDeleteClick = { viewModel.deleteModule(module.id) }
                    )
                }
            }
        }
    }

    if (uiState.editingModuleId != null) {
        EditDialog(
            title = "Editar módulo",
            name = uiState.editingModuleName,
            onNameChange = { viewModel.onEditModuleNameChange(it) },
            nameLabel = "Nombre del módulo",
            description = uiState.editingModuleDescription,
            onDescriptionChange = { viewModel.onEditModuleDescriptionChange(it) },
            onConfirm = { viewModel.saveEditModule() },
            onDismiss = { viewModel.cancelEditModule() }
        )
    }
}

@Composable
private fun ModuleItem(
    module: ModuleResponse,
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
                    text = module.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = module.description,
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
