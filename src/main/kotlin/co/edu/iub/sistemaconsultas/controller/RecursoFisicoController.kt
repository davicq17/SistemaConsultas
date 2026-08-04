package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.recursoFisico.RecursoFisicoResponse
import co.edu.iub.sistemaconsultas.dto.recursoFisico.RegistroRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.dto.recursoFisico.UpdateRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.service.RecursoFisicoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Recursos físicos",
    description = "Gestión de recursos físicos."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/recursos_fisicos")
class RecursoFisicoController(
    private val rfService: RecursoFisicoService
) {

    @Operation(
        summary = "Registrar recurso físico",
        description = "Crea un nuevo recurso físico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "RF creado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarRF(
        @Valid
        @RequestBody
        request: RegistroRecursoFisicoRequest
    ): RecursoFisicoResponse{
        return rfService.registrarRecursoFisico(request)
    }

    @Operation(
        summary = "Listar recursos físicos",
        description = "Obtiene todos los recursos físicos."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarRF(): List<RecursoFisicoResponse> {
        return rfService.listarRecursosFisicos()
    }

    @Operation(
        summary = "Buscar recurso físico por ID",
        description = "Obtiene la información de un recurso físico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "RF encontrado."),
            ApiResponse(responseCode = "404", description = "RF no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerRFPorId(@PathVariable id: Long): RecursoFisicoResponse{
        return rfService.obtenerRecursoFisicoPorId(id)
    }

    @Operation(
        summary = "Actualizar recurso físico",
        description = "Actualiza la información de un recurso físico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "RF actualizado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "RF no encontrado.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarRF(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: UpdateRecursoFisicoRequest
    ): RecursoFisicoResponse{
        return rfService.actualizarRecursoFisico(id, request)
    }

    @Operation(
        summary = "Eliminar recurso físico",
        description = "Realiza la eliminación lógica de un recurso físico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "RF eliminado correctamente."),
            ApiResponse(responseCode = "404", description = "RF no encontrado.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarRF(@PathVariable id: Long){
        rfService.eliminarRecursoFisico(id)
    }
}