package com.example.iubconsultas.ui.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.auth.REGISTRATION_ROLES
import com.example.iubconsultas.ui.component.ClickableText
import com.example.iubconsultas.ui.component.EmailField
import com.example.iubconsultas.ui.component.InputField
import com.example.iubconsultas.ui.component.PasswordField
import com.example.iubconsultas.ui.component.PrimaryButton
import com.example.iubconsultas.ui.component.RoleSelector

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completa tu información",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            RegisterFormContent(
                viewModel = viewModel,
                onSuccess = onRegisterSuccess
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                onClick = onLoginClick
            )
        }
    }
}

@Composable
fun RegisterFormContent(
    viewModel: RegisterViewModel = viewModel(),
    onSuccess: () -> Unit = {}
) {
    val uiState = viewModel.uiState

    Column {
        InputField(
            value = uiState.identification,
            onValueChange = { viewModel.onIdentificationChange(it) },
            label = "Identificación",
            error = uiState.error
        )

        InputField(
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = "Nombre",
            error = uiState.error
        )

        InputField(
            value = uiState.lastName,
            onValueChange = { viewModel.onLastNameChange(it) },
            label = "Apellido",
            error = uiState.error
        )

        EmailField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            error = uiState.error
        )

        PasswordField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            error = uiState.error
        )

        RoleSelector(
            value = uiState.role,
            onValueChange = { viewModel.onRoleChange(it) },
            options = REGISTRATION_ROLES
        )

        PrimaryButton(
            text = if (uiState.isLoading) "Cargando..." else "Registrarse",
            onClick = { viewModel.register(onSuccess = onSuccess) },
            enabled = !uiState.isLoading
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
