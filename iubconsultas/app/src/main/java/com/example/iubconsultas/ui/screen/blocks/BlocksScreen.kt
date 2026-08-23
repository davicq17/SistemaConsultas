package com.example.iubconsultas.ui.screen.blocks

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
import com.example.iubconsultas.data.remote.dto.catalog.BlockResponse
import com.example.iubconsultas.ui.component.DropdownSelector
import com.example.iubconsultas.ui.component.EditDialog
import com.example.iubconsultas.ui.component.InputField
import com.example.iubconsultas.ui.component.PrimaryButton

@Composable
fun BlocksContent(
    viewModel: BlocksViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        InputField(
            value = uiState.newBlockName,
            onValueChange = { viewModel.onNewBlockNameChange(it) },
            label = "Nombre del bloque",
            error = uiState.error
        )

        DropdownSelector(
            label = "Sede",
            options = uiState.sedes.map { it.name },
            selectedOption = uiState.selectedSedeName,
            onOptionSelected = { viewModel.onSedeSelected(it) }
        )

        PrimaryButton(
            text = "Agregar",
            onClick = { viewModel.addBlock() }
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
                items(uiState.blocks, key = { it.id }) { block ->
                    BlockItem(
                        block = block,
                        onEditClick = { viewModel.startEditBlock(block) },
                        onDeleteClick = { viewModel.deleteBlock(block.id) }
                    )
                }
            }
        }
    }

    if (uiState.editingBlockId != null) {
        EditDialog(
            title = "Editar bloque",
            name = uiState.editingBlockName,
            onNameChange = { viewModel.onEditBlockNameChange(it) },
            nameLabel = "Nombre del bloque",
            onConfirm = { viewModel.saveEditBlock() },
            onDismiss = { viewModel.cancelEditBlock() }
        )
    }
}

@Composable
private fun BlockItem(
    block: BlockResponse,
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
                    text = block.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = block.sedeName,
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
