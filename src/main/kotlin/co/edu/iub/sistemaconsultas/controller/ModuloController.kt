package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.ModuloResponse
import co.edu.iub.sistemaconsultas.dto.RegistroModuloRequest
import co.edu.iub.sistemaconsultas.dto.UpdateModuloRequest
import co.edu.iub.sistemaconsultas.service.ModuloService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/modulos")
class ModuloController(
    private val moduloService: ModuloService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarModulo(
        @RequestBody
        request: RegistroModuloRequest
    ): ModuloResponse{

        return moduloService.registrarModulo(request)

    }

    @GetMapping
    fun listarModulos(): List<ModuloResponse>{

        return moduloService.listarModulos()

    }

    @GetMapping("/{id}")
    fun obtenerModulo(@PathVariable id: Long): ModuloResponse{

        return moduloService.obtenerModuloPorId(id)

    }

    @PutMapping("{id}")
    fun actualizarModulo(
        @PathVariable
        id: Long,
        @RequestBody
        request: UpdateModuloRequest
    ): ModuloResponse{

        return moduloService.actualizarModulo(id, request)

    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarModulo(
        @PathVariable
        id: Long,
    ){

        return moduloService.eliminarModulo(id)

    }
}