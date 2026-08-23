package com.example.iubconsultas.data.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.iubconsultas.data.remote.dto.auth.Role

object SessionManager {
    var token: String? = null
        private set

    var userId: Long? = null
        private set

    var email: String? = null
        private set

    var name: String? = null
        private set

    var role: Role? = null
        private set

    var sessionExpired by mutableStateOf(false)
        private set

    fun saveSession(token: String, userId: Long, email: String, name: String, role: Role) {
        this.token = token
        this.userId = userId
        this.email = email
        this.name = name
        this.role = role
        this.sessionExpired = false
    }

    fun isLoggedIn(): Boolean = token != null

    fun expireSession() {
        sessionExpired = true
    }

    fun logout() {
        token = null
        userId = null
        email = null
        name = null
        role = null
    }
}
