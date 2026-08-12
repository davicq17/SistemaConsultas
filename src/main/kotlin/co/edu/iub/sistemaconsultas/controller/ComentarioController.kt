package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.comentario.ComentarioResponse
import co.edu.iub.sistemaconsultas.dto.comentario.RegistroComentarioRequest
import co.edu.iub.sistemaconsultas.dto.comentario.UpdateComentarioRequest
import co.edu.iub.sistemaconsultas.service.ComentarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Comentarios",
    description = "Gestión de comentarios."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/comentarios")
class ComentarioController(
    private val comentarioService: ComentarioService
) {

    @Operation(
        summary = "Registrar comentario",
        description = "Crea un nuevo comentario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Comentario creado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun registrarComentario(
        @Valid
        @RequestBody
        request: RegistroComentarioRequest
    ): ComentarioResponse{
        return comentarioService.registrarComentario(request)
    }

    @Operation(
        summary = "Buscar comentario por ID",
        description = "Obtiene la información de un comentario específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comentario encontrado."),
            ApiResponse(responseCode = "404", description = "Comentario no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerComentarioPorId(
        @PathVariable id: Long
    ): ComentarioResponse{
        return comentarioService.obtenerComentarioPorId(id)
    }

    @Operation(
        summary = "Listar comentarios",
        description = "Obtiene todos los comentarios de la solicitud."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping("/solicitud/{solicitudId}")
    fun listarComentariosPorSolicitud(
        @PathVariable solicitudId: Long
    ): List<ComentarioResponse>{
        return comentarioService.listarComentariosPorSolicitud(solicitudId)
    }

    @Operation(
        summary = "Editar comentario",
        description = "Editar el contenido de un comentario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comentario editado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Comentario no encontrado.")
        ]
    )
    @PutMapping("/{id}")
    fun editarComentario(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: UpdateComentarioRequest
    ): ComentarioResponse{
        return comentarioService.actualizarComentario(id, request)
    }
}