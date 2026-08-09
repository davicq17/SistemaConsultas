package co.edu.iub.sistemaconsultas.util

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    fun obtenerCorreo(): String =
        SecurityContextHolder
            .getContext()
            .authentication
            ?.name
            ?: throw IllegalArgumentException("No hay usuario autenticado.")
}