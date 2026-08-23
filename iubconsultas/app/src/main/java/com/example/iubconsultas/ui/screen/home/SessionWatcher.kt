package com.example.iubconsultas.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.iubconsultas.data.local.SessionManager

@Composable
fun SessionWatcher(onExpired: () -> Unit) {
    LaunchedEffect(SessionManager.sessionExpired) {
        if (SessionManager.sessionExpired) {
            SessionManager.logout()
            onExpired()
        }
    }
}
