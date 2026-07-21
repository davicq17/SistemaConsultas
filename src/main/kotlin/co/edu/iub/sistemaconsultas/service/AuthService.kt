package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest

interface AuthService{

    fun login(request: LoginRequest): LoginResponse

    fun registrar(request: RegistroUsuarioRequest)
}