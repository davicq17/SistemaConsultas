package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.dto.RegistroProgAcademicoRequest
import co.edu.iub.sistemaconsultas.dto.UpdateProgAcademicoRequest
import co.edu.iub.sistemaconsultas.service.ProgAcademicoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "Programas Académicos",
    description = "Gestión de programas académicos del sistema."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/programas")
class ProgAcademicoController(
    private val progAcademicoService: ProgAcademicoService
) {

    @Operation(
        summary = "Registrar programa académico",
        description = "Crea un nuevo programa académico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Programa creado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarPrograma(
        @Valid @RequestBody request: RegistroProgAcademicoRequest
    ): ProgAcademicoResponse{

        return progAcademicoService.registrarPrograma(request)
    }

    @Operation(
        summary = "Listar programas académicos",
        description = "Obtiene todos los programas académicos activos."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarProgramas(): List<ProgAcademicoResponse>{

        return progAcademicoService.listarProgramas()
    }

    @Operation(
        summary = "Buscar programa académico por ID",
        description = "Obtiene la información de un programa académico específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Programa encontrado."),
            ApiResponse(responseCode = "404", description = "Programa no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerPrograma(
        @PathVariable
        id: Long
    ): ProgAcademicoResponse{

        return progAcademicoService.obtenerProgramaPorId(id)
    }

    @Operation(
        summary = "Actualizar programa académico",
        description = "Actualiza la información de un programa académico existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Programa actualizado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Programa no encontrado.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarPromaga(

        @PathVariable
        id:Long,

        @Valid
        @RequestBody
        request: UpdateProgAcademicoRequest
    ): ProgAcademicoResponse{

        return progAcademicoService.actualizarPrograma(id, request)
    }

    @Operation(
        summary = "Eliminar programa académico",
        description = "Realiza la eliminación lógica de un programa académico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Programa eliminado correctamente."),
            ApiResponse(responseCode = "400", description = "El programa tiene usuarios activos asociados."),
            ApiResponse(responseCode = "404", description = "Programa no encontrado.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarPrograma(
        @PathVariable
        id:Long
    ){
        return progAcademicoService.eliminarPrograma(id)
    }
}