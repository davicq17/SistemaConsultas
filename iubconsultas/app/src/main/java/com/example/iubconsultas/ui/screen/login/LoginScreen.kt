package com.example.iubconsultas.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.iubconsultas.data.local.SessionManager
import com.example.iubconsultas.ui.component.ClickableText
import com.example.iubconsultas.ui.component.EmailField
import com.example.iubconsultas.ui.component.PasswordField
import com.example.iubconsultas.ui.component.PrimaryButton

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge
            )

            if (SessionManager.sessionExpired) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tu sesión expiró por seguridad. Vuelve a iniciar " +
                        "sesión para continuar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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

            PrimaryButton(
                text = if (uiState.isLoading) "Cargando..." else "Iniciar sesión",
                onClick = { viewModel.login(onSuccess = onLoginSuccess) },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = "¿No tienes una cuenta? Regístrate aquí",
                onClick = onRegisterClick
            )
        }
    }
}
