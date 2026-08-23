package com.example.iubconsultas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iubconsultas.data.local.SessionManager
import com.example.iubconsultas.data.remote.dto.auth.Role
import com.example.iubconsultas.ui.screen.home.AdminHomeScreen
import com.example.iubconsultas.ui.screen.home.StudentHomeScreen
import com.example.iubconsultas.ui.screen.home.TeacherHomeScreen
import com.example.iubconsultas.ui.screen.login.LoginScreen
import com.example.iubconsultas.ui.screen.register.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {
        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(homeRouteForCurrentUser()) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppDestination.Register.route)
                }
            )
        }

        composable(AppDestination.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppDestination.AdminHome.route) {
            AdminHomeScreen(
                onLogout = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.AdminHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.TeacherHome.route) {
            TeacherHomeScreen(
                onLogout = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.TeacherHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.StudentHome.route) {
            StudentHomeScreen(
                onLogout = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.StudentHome.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun homeRouteForCurrentUser(): String = when (SessionManager.role) {
    Role.ADMINISTRATOR -> AppDestination.AdminHome.route
    Role.TEACHER -> AppDestination.TeacherHome.route
    else -> AppDestination.StudentHome.route
}
