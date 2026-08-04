package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.bloque.BloqueResponse
import co.edu.iub.sistemaconsultas.dto.bloque.RegistroBloqueRequest
import co.edu.iub.sistemaconsultas.dto.bloque.UpdateBloqueRequest
import co.edu.iub.sistemaconsultas.service.BloqueService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Bloques",
    description = "Gestión de bloques académicos."
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/bloques")
class BloqueController(
    private val bloqueService: BloqueService
) {

    @Operation(
        summary = "Registrar bloque",
        description = "Crea un nuevo bloque académico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Bloque creado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos.")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarBloque(
        @Valid
        @RequestBody
        request: RegistroBloqueRequest
    ): BloqueResponse{
        return bloqueService.registrarBloque(request)
    }

    @Operation(
        summary = "Listar bloques",
        description = "Obtiene todos los bloques activos."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Consulta realizada correctamente."),
            ApiResponse(responseCode = "401", description = "No autenticado.")
        ]
    )
    @GetMapping
    fun listarBloques(): List<BloqueResponse>{
        return bloqueService.listarBloques()
    }

    @Operation(
        summary = "Buscar bloque por ID",
        description = "Obtiene la información de un bloque específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Bloque encontrado."),
            ApiResponse(responseCode = "404", description = "Bloque no encontrado.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerBloque(@PathVariable id:Long): BloqueResponse{
        return bloqueService.obtenerBloquePorId(id)
    }

    @Operation(
        summary = "Actualizar bloque",
        description = "Actualiza la información de un bloque existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Bloque actualizado correctamente."),
            ApiResponse(responseCode = "400", description = "Datos inválidos."),
            ApiResponse(responseCode = "404", description = "Bloque no encontrado.")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarBloque(
        @PathVariable id: Long,
        @Valid
        @RequestBody
        request: UpdateBloqueRequest
    ): BloqueResponse{
        return bloqueService.actualizarBloque(id, request)
    }

    @Operation(
        summary = "Eliminar bloque",
        description = "Realiza la eliminación lógica de un bloque."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Bloque eliminado correctamente."),
            ApiResponse(responseCode = "404", description = "Bloque no encontrado.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarBloque(@PathVariable id: Long){
        bloqueService.eliminarBloque(id)
    }
}

