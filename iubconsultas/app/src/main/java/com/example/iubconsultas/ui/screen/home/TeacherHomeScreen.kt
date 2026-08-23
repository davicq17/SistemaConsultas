package com.example.iubconsultas.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.ui.component.AppHeader
import com.example.iubconsultas.ui.screen.consultations.ConsultationsListContent
import com.example.iubconsultas.ui.screen.consultations.ConsultationsViewModel

@Composable
fun TeacherHomeScreen(
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val consultationsViewModel: ConsultationsViewModel = viewModel()

    SessionWatcher(onExpired = onLogout)

    val myConsultations = consultationsViewModel.uiState.consultations.filter {
        it.teacherIdentification == uiState.identification
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Portal de Docentes",
                userName = uiState.userName,
                onLogout = { viewModel.logout(onLoggedOut = onLogout) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ConsultationsListContent(
                consultations = myConsultations,
                role = Role.TEACHER,
                viewModel = consultationsViewModel,

                showTeacherFilter = false,
                showStudentFilter = true
            )
        }
    }
}
