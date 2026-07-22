package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.ForgotPasswordRequest
import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.ResetPasswordRequest

interface AuthService{

    fun login(request: LoginRequest): LoginResponse

    fun registrar(request: RegistroUsuarioRequest)

    fun forgotPassword(request: ForgotPasswordRequest)

    fun resetPassword(request: ResetPasswordRequest)
}