package com.example.iubconsultas.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.data.remote.dto.auth.label

@Composable
fun RoleSelector(
    value: Role,
    onValueChange: (Role) -> Unit,
    options: List<Role> = Role.entries
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = value.label(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Rol") },
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role.label()) },
                    onClick = {
                        onValueChange(role)
                        expanded = false
                    }
                )
            }
        }
    }
}