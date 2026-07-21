package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
){
    @PostMapping("/register")
    fun registrar(
        @RequestBody request: RegistroUsuarioRequest
    ): ResponseEntity<String> {

        authService.registrar(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Usuario registrado correctamente.")

    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {

        val response = authService.login(request)

        return ResponseEntity.ok(response)

    }
}