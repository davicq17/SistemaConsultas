package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.solicitud.*
import co.edu.iub.sistemaconsultas.service.SolicitudConsultaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/solicitudes-consultas")
class SolicitudConsultaController(
    private val solicitudConsultaService: SolicitudConsultaService
) {

    @GetMapping
    fun listarTodas(): ResponseEntity<List<SolicitudConsultaResponse>> {
        val solicitudes = solicitudConsultaService.listarTodas()
        return ResponseEntity.ok(solicitudes)
    }

    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Long): ResponseEntity<SolicitudConsultaResponse> {
        val solicitud = solicitudConsultaService.obtenerPorId(id)
        return ResponseEntity.ok(solicitud)
    }

    @PostMapping
    fun registrar(@RequestBody request: RegistroSolicitudConsultaRequest): ResponseEntity<SolicitudConsultaResponse> {
        val nuevaSolicitud = solicitudConsultaService.registrar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud)
    }

    @PutMapping("/{id}")
    fun actualizar(
        @PathVariable id: Long,
        @RequestBody request: UpdateSolicitudConsultaRequest
    ): ResponseEntity<SolicitudConsultaResponse> {
        val actualizada = solicitudConsultaService.actualizar(id, request)
        return ResponseEntity.ok(actualizada)
    }

    @PatchMapping("/{id}/recurso-fisico")
    fun asignarRecursoFisico(
        @PathVariable id: Long,
        @RequestBody request: AsignarRecursoFisicoRequest
    ): ResponseEntity<SolicitudConsultaResponse> {
        val actualizada = solicitudConsultaService.asignarRecursoFisico(id, request)
        return ResponseEntity.ok(actualizada)
    }

    @PatchMapping("/{id}/estado")
    fun cambiarEstado(
        @PathVariable id: Long,
        @RequestBody request: CambiarEstadoSolicitudRequest
    ): ResponseEntity<SolicitudConsultaResponse> {
        val actualizada = solicitudConsultaService.cambiarEstado(id, request)
        return ResponseEntity.ok(actualizada)
    }

    @PatchMapping("/{id}/docente")
    fun reasignarDocente(
        @PathVariable id: Long,
        @RequestBody request: ReasignarDocenteRequest
    ): ResponseEntity<SolicitudConsultaResponse> {
        val actualizada = solicitudConsultaService.reasignarDocente(id, request)
        return ResponseEntity.ok(actualizada)
    }
}