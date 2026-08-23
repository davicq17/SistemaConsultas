package com.example.iubconsultas.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iubconsultas.data.local.SessionManager
import com.example.iubconsultas.data.remote.RetrofitClient
import com.example.iubconsultas.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val userRepository = UserRepository(RetrofitClient.apiService)

    var uiState by mutableStateOf(
        HomeUiState(userName = SessionManager.name ?: "")
    )
        private set

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        val id = SessionManager.userId ?: return

        viewModelScope.launch {
            try {
                val response = userRepository.getUser(id)
                val user = response.body()

                if (response.isSuccessful && user != null) {
                    uiState = uiState.copy(
                        userName = "${user.name} ${user.lastName}",
                        identification = user.identification
                    )
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onTabSelected(index: Int) {
        uiState = uiState.copy(selectedTab = index)
    }

    fun logout(onLoggedOut: () -> Unit) {
        SessionManager.logout()
        onLoggedOut()
    }
}
