package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.ForgotPasswordRequest
import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.ResetPasswordRequest
import co.edu.iub.sistemaconsultas.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Authenticación",
    description = "Endpoints para autenticación, registro y recuperación de contraseña."
)
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
){

    @Operation(
        summary = "Registrar usuario",
        description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Usuario registrado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping("/register")
    fun registrar(
        @Valid @RequestBody request: RegistroUsuarioRequest
    ): ResponseEntity<String> {

        authService.registrar(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Usuario registrado correctamente.")

    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario mediante correo y contraseña y devuelve un token JWT."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Autenticación exitosa."),
            ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            ApiResponse(responseCode = "401", description = "Credenciales incorrectas.")
        ]
    )
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {

        val response = authService.login(request)

        return ResponseEntity.ok(response)

    }

    @Operation(
        summary = "Solicitar recuperación de contraseña",
        description = "Genera un token de recuperación y lo envía al correo del usuario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Solicitud procesada correctamente."),
            ApiResponse(responseCode = "400", description = "Correo inválido.")
        ]
    )
    @PostMapping("/forgot-password")
    fun forgotPassword(
        @RequestBody request: ForgotPasswordRequest
    ): ResponseEntity<String>{

        authService.forgotPassword(request)

        return ResponseEntity.ok(
            "Se ha generado un token de recuperación."
        )
    }

    @Operation(
        summary = "Restablecer contraseña",
        description = "Permite actualizar la contraseña utilizando un token de recuperación válido."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente."),
            ApiResponse(responseCode = "400", description = "Token inválido o expirado.")
        ]
    )
    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestBody request: ResetPasswordRequest
    ): ResponseEntity<String>{

        authService.resetPassword(request)

        return ResponseEntity.ok(
            "Contraseña actualizada correctamente."
        )
    }
}