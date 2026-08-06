package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.solicitud.*
import co.edu.iub.sistemaconsultas.service.SolicitudConsultaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Solicitudes de Consulta",
    description = "Gestión de solicitudes de consulta."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/solicitudes-consultas")
class SolicitudConsultaController(
    private val solicitudConsultaService: SolicitudConsultaService
) {

    @Operation(
        summary = "Registrar solicitud de consulta",
        description = "Crea una nueva solicitud de consulta."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Solicitud creada correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrar(
        @Valid
        @RequestBody request: RegistroSolicitudConsultaRequest
    ):SolicitudConsultaResponse {
        return solicitudConsultaService.registrar(request)
    }

    @Operation(
        summary = "Listar solicitudes de consultas",
        description = "Obtiene todas las solicitudes de consultas sin importar su estado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarTodas():List<SolicitudConsultaResponse> {
        return solicitudConsultaService.listarTodas()
    }

    @Operation(
        summary = "Buscar solicitud de consulta por ID",
        description = "Obtiene la información de una solicitud de consulta en especifico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Solicitud de consulta encontrada."),
            ApiResponse(responseCode = "404", description = "Solicitud de consulta no encontrada.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Long): SolicitudConsultaResponse {
        return solicitudConsultaService.obtenerPorId(id)
    }

    @Operation(
        summary = "Actualizar solicitud de consulta",
        description = "Actualiza la información de una solicitud de consulta como alumno."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Solicitud de consulta actualizada correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Solicitud de consulta no encontrada.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizar(
        @PathVariable id: Long,
        @Valid
        @RequestBody request: UpdateSolicitudConsultaRequest
    ): SolicitudConsultaResponse {
        return solicitudConsultaService.actualizar(id, request)
    }

    @Operation(
        summary = "Actualizar estado",
        description = "Actualiza el estado de una solicitud de consulta."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado actualizado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Solicitud de consulta no encontrada."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @PatchMapping("/{id}/estado")
    fun cambiarEstado(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: CambiarEstadoSolicitudRequest
    ): SolicitudConsultaResponse {
        return solicitudConsultaService.cambiarEstado(id, request)
    }

    @Operation(
        summary = "Reasignar docente",
        description = "Reasigna el docente de una solicitud de consulta."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Docente reasignado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Solicitud de consulta no encontrada."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @PatchMapping("/{id}/docente")
    fun reasignarDocente(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: ReasignarDocenteRequest
    ): SolicitudConsultaResponse {
        return solicitudConsultaService.reasignarDocente(id, request)
    }

    @Operation(
        summary = "Asignar recurso físico",
        description = "Asigna un recurso físico a una consulta."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Recurso físico asignado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Solicitud de consulta no encontrada."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @PatchMapping("/{id}/recurso-fisico")
    fun asignarRecursoFisico(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: AsignarRecursoFisicoRequest
    ): SolicitudConsultaResponse {
        return solicitudConsultaService.asignarRecursoFisico(id, request)
    }


}