package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.ModuloResponse
import co.edu.iub.sistemaconsultas.dto.RegistroModuloRequest
import co.edu.iub.sistemaconsultas.dto.UpdateModuloRequest
import co.edu.iub.sistemaconsultas.service.ModuloService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "Módulos",
    description = "Gestión de módulos académicos."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/modulos")
class ModuloController(
    private val moduloService: ModuloService
) {

    @Operation(
        summary = "Registrar módulo",
        description = "Crea un nuevo módulo académico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Módulo creado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarModulo(
        @Valid
        @RequestBody
        request: RegistroModuloRequest
    ): ModuloResponse{

        return moduloService.registrarModulo(request)

    }

    @Operation(
        summary = "Listar módulos",
        description = "Obtiene todos los módulos activos."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarModulos(): List<ModuloResponse>{

        return moduloService.listarModulos()

    }

    @Operation(
        summary = "Buscar módulo por ID",
        description = "Obtiene la información de un módulo específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Módulo encontrado."),
            ApiResponse(responseCode = "404", description = "Módulo no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerModulo(@PathVariable id: Long): ModuloResponse{

        return moduloService.obtenerModuloPorId(id)

    }

    @Operation(
        summary = "Actualizar módulo",
        description = "Actualiza la información de un módulo existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Módulo actualizado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Módulo no encontrado.")
        ]
    )
    @PutMapping("{id}")
    fun actualizarModulo(
        @PathVariable
        id: Long,
        @Valid
        @RequestBody
        request: UpdateModuloRequest
    ): ModuloResponse{

        return moduloService.actualizarModulo(id, request)

    }

    @Operation(
        summary = "Eliminar módulo",
        description = "Realiza la eliminación lógica de un módulo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Módulo eliminado correctamente."),
            ApiResponse(responseCode = "404", description = "Módulo no encontrado.")
        ]
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarModulo(
        @PathVariable
        id: Long,
    ){

        return moduloService.eliminarModulo(id)

    }
}