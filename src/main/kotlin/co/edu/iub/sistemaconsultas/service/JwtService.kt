package co.edu.iub.sistemaconsultas.service

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {

    fun generarToken(userDetails: UserDetails): String

    fun extraerCorreo(token: String): String

    fun esTokenValido(token: String, userDetails: UserDetails): Boolean

}