package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.dto.RegistroProgAcademicoRequest
import co.edu.iub.sistemaconsultas.dto.UpdateProgAcademicoRequest
import co.edu.iub.sistemaconsultas.service.ProgAcademicoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/programas")
class ProgAcademicoController(
    private val progAcademicoService: ProgAcademicoService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registrarPrograma(
        @Valid @RequestBody request: RegistroProgAcademicoRequest
    ): ProgAcademicoResponse{

        return progAcademicoService.registrarPrograma(request)
    }

    @GetMapping
    fun listarProgramas(): List<ProgAcademicoResponse>{

        return progAcademicoService.listarProgramas()
    }

    @GetMapping("/{id}")
    fun obtenerPrograma(
        @PathVariable
        id: Long
    ): ProgAcademicoResponse{

        return progAcademicoService.obtenerProgramaPorId(id)
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarPrograma(
        @PathVariable
        id:Long
    ){
        return progAcademicoService.eliminarPrograma(id)
    }
}