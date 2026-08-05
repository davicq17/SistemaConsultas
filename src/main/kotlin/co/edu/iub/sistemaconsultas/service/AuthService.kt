package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.auth.ForgotPasswordRequest
import co.edu.iub.sistemaconsultas.dto.auth.LoginRequest
import co.edu.iub.sistemaconsultas.dto.auth.LoginResponse
import co.edu.iub.sistemaconsultas.dto.usuario.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.auth.ResetPasswordRequest

interface AuthService{

    fun login(request: LoginRequest): LoginResponse

    fun registrar(request: RegistroUsuarioRequest)

    fun forgotPassword(request: ForgotPasswordRequest)

    fun resetPassword(request: ResetPasswordRequest)
}