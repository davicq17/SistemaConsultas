package com.example.iubconsultas.ui.navigation

sealed class AppDestination(val route: String) {
    object Login : AppDestination("login")
    object Register : AppDestination("register")

    object AdminHome : AppDestination("admin_home")
    object TeacherHome : AppDestination("teacher_home")
    object StudentHome : AppDestination("student_home")
}
