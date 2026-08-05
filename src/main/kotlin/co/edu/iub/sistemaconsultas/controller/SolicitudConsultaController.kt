package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.solicitud.RegistroSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.dto.solicitud.UpdateSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.service.SolicitudService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/solicitudes-consulta")
class SolicitudConsultaController(
    private val solicitudService: SolicitudService
) {

    @PostMapping
    fun crear(@Valid @RequestBody request: RegistroSolicitudConsultaRequest): ResponseEntity<SolicitudConsultaResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.crear(request))
    }

    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Long): ResponseEntity<SolicitudConsultaResponse> {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id))
    }

    @GetMapping
    fun listar(): ResponseEntity<List<SolicitudConsultaResponse>> {
        return ResponseEntity.ok(solicitudService.listarTodas())
    }

    @PutMapping("/{id}")
    fun actualizar(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateSolicitudConsultaRequest
    ): ResponseEntity<SolicitudConsultaResponse> {
        return ResponseEntity.ok(solicitudService.actualizar(id, request))
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        solicitudService.eliminar(id)
        return ResponseEntity.noContent().build()
    }
}