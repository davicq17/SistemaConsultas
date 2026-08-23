package com.example.iubconsultas.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.ui.component.AppHeader
import com.example.iubconsultas.ui.screen.consultations.ConsultationFormContent
import com.example.iubconsultas.ui.screen.consultations.ConsultationsListContent
import com.example.iubconsultas.ui.screen.consultations.ConsultationsViewModel

private val STUDENT_TABS = listOf(
    "Mis Consultas",
    "Solicitar Consulta"
)

@Composable
fun StudentHomeScreen(
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val consultationsViewModel: ConsultationsViewModel = viewModel()

    SessionWatcher(onExpired = onLogout)

    val myConsultations = consultationsViewModel.uiState.consultations.filter {
        it.studentIdentification == uiState.identification
    }

    Scaffold(
        topBar = {
            Column {
                AppHeader(
                    title = "Portal del Estudiante",
                    userName = uiState.userName,
                    onLogout = { viewModel.logout(onLoggedOut = onLogout) }
                )

                PrimaryTabRow(selectedTabIndex = uiState.selectedTab) {
                    STUDENT_TABS.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.selectedTab == index,
                            onClick = { viewModel.onTabSelected(index) },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                0 -> ConsultationsListContent(
                    consultations = myConsultations,
                    role = Role.STUDENT,
                    viewModel = consultationsViewModel,
                    showTeacherFilter = true,

                    showStudentFilter = false
                )

                1 -> ConsultationFormContent(viewModel = consultationsViewModel)
            }
        }
    }
}
