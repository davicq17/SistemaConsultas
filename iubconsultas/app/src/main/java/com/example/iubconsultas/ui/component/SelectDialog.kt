package com.example.iubconsultas.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SelectDialog(
    title: String,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    reason: String? = null,
    onReasonChange: (String) -> Unit = {},
    reasonLabel: String = "Motivo",
    error: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                if (options.isEmpty()) {
                    Text(
                        text = error ?: "No hay opciones disponibles",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    DropdownSelector(
                        label = label,
                        options = options,
                        selectedOption = selectedOption,
                        onOptionSelected = onOptionSelected
                    )

                    if (reason != null) {
                        InputField(
                            value = reason,
                            onValueChange = onReasonChange,
                            label = reasonLabel
                        )
                    }

                    if (error != null) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (options.isNotEmpty()) {
                TextButton(onClick = onConfirm) {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (options.isEmpty()) "Cerrar" else "Cancelar")
            }
        }
    )
}
