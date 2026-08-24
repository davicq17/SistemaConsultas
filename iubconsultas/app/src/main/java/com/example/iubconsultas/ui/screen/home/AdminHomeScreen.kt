package com.example.iubconsultas.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.ui.component.AppHeader
import com.example.iubconsultas.ui.screen.blocks.BlocksContent
import com.example.iubconsultas.ui.screen.consultations.ConsultationsListContent
import com.example.iubconsultas.ui.screen.consultations.ConsultationsViewModel
import com.example.iubconsultas.ui.screen.modules.ModulesContent
import com.example.iubconsultas.ui.screen.programs.ProgramsContent
import com.example.iubconsultas.ui.screen.register.RegisterFormContent
import com.example.iubconsultas.ui.screen.resources.ResourcesContent
import com.example.iubconsultas.ui.screen.sedes.SedesContent
import com.example.iubconsultas.ui.screen.users.UsersContent
import com.example.iubconsultas.ui.screen.users.UsersViewModel

private val ADMIN_TABS = listOf(
    "Consultas",
    "Usuarios",
    "Registrar Usuario",
    "Programas",
    "Módulos",
    "Sedes",
    "Bloques",
    "Recursos"
)

@Composable
fun AdminHomeScreen(
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val consultationsViewModel: ConsultationsViewModel = viewModel()
    val usersViewModel: UsersViewModel = viewModel()

    SessionWatcher(onExpired = onLogout)

    Scaffold(
        topBar = {
            Column {
                AppHeader(
                    title = "Portal del Administrador",
                    userName = uiState.userName,
                    onLogout = { viewModel.logout(onLoggedOut = onLogout) }
                )

                PrimaryScrollableTabRow(selectedTabIndex = uiState.selectedTab) {
                    ADMIN_TABS.forEachIndexed { index, title ->
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
                    consultations = consultationsViewModel.uiState.consultations,
                    role = Role.ADMINISTRATOR,
                    viewModel = consultationsViewModel
                )

                1 -> UsersContent(viewModel = usersViewModel)

                2 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    RegisterFormContent(
                        onSuccess = { usersViewModel.loadUsers() }
                    )
                }

                3 -> ProgramsContent()
                4 -> ModulesContent()
                5 -> SedesContent()
                6 -> BlocksContent()
                7 -> ResourcesContent()
            }
        }
    }
}
