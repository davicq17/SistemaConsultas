package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.sede.RegistroSedeRequest
import co.edu.iub.sistemaconsultas.dto.sede.SedeResponse
import co.edu.iub.sistemaconsultas.dto.sede.UpdateSedeRequest
import co.edu.iub.sistemaconsultas.service.SedeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Sedes",
    description = "Gestión de sedes académicas."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/sedes")
class SedeController(
    private val sedeService: SedeService
) {

    @Operation(
        summary = "Registrar sede",
        description = "Crea una nueva sede académica."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Sede creada correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarSede(
        @Valid
        @RequestBody
        request: RegistroSedeRequest
    ): SedeResponse{
        return sedeService.registrarSede(request)
    }

    @Operation(
        summary = "Listar sedes",
        description = "Obtiene todas las sedes activas."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarSedes(): List<SedeResponse>{

        return sedeService.listarSedes()

    }

    @Operation(
        summary = "Buscar sede por ID",
        description = "Obtiene la información de una sede específica."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sede encontrada."),
            ApiResponse(responseCode = "404", description = "Sede no encontrada.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerSede(@PathVariable id: Long): SedeResponse{

        return sedeService.obtenerSedePorId(id)

    }

    @Operation(
        summary = "Actualizar sede",
        description = "Actualiza la información de una sede existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sede actualizada correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Sede no encontrada.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarSede(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: UpdateSedeRequest
    ): SedeResponse{

        return sedeService.actualizarSede(id,request)

    }

    @Operation(
        summary = "Eliminar sede",
        description = "Realiza la eliminación lógica de una sede."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Sede eliminada correctamente."),
            ApiResponse(responseCode = "404", description = "Sede no encontrada.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarSede(
        @PathVariable id:Long
    ){

        sedeService.eliminarSede(id)

    }
}