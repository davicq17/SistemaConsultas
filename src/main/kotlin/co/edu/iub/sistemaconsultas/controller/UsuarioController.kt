package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.usuario.UpdateUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.usuario.UsuarioResponse
import co.edu.iub.sistemaconsultas.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "Usuarios",
    description = "Gestión de usuarios del sistema."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/usuarios")
class UsuarioController (
    private val usuarioService: UsuarioService
){

    @Operation(
        summary = "Listar usuarios",
        description = "Obtiene todos los usuarios activos del sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarUsuarios(): List<UsuarioResponse>{
        return usuarioService.listarUsuarios()
    }

    @Operation(
        summary = "Buscar usuario por ID",
        description = "Obtiene la información de un usuario específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerUsuarioPorId(
        @PathVariable id: Long
    ): UsuarioResponse{
        return usuarioService.obtenerUsuarioPorId(id)
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza la información de un usuario existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuario actualizado."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarUsuario(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUsuarioRequest
    ): UsuarioResponse{
        return usuarioService.actualizarUsuario(id, request)
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Realiza la eliminación lógica de un usuario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Usuario eliminado."),
            ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
        ]
    )
    @DeleteMapping("/{id}")
    fun eliminarUsuario(
        @PathVariable id: Long
    ): ResponseEntity<Void>{

        usuarioService.eliminarUsuario(id)

        return ResponseEntity.noContent().build()
    }
}