package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.notificacion.NotificacionResponse
import co.edu.iub.sistemaconsultas.service.NotificacionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Notificaciones",
    description = "Endpoints para la bandeja de notificaciones de un usuario."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/notificaciones")
class NotificacionController(
    private val notificacionService: NotificacionService
) {

    @Operation(
        summary = "Listar Notificaciones",
        description = "Obtiene la bandeja de notificaciones del usuario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarNotificaciones(): List<NotificacionResponse>{
        return  notificacionService.listarNotificaciones()
    }

    @Operation(
        summary = "Contar Notificaciones",
        description = "Obtiene el numero de notificaciones no leídas del usuario."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping("/no-leidas/count")
    fun contarNotificacionesLeidas(): Long{
        return notificacionService.contarNotificacionesNoLeidas()
    }

    @PatchMapping("/{id}/leer")
    fun marcarComoLeida(
        @PathVariable("id") id: Long
    ): NotificacionResponse{
        return notificacionService.marcarComoLeida(id)
    }


}