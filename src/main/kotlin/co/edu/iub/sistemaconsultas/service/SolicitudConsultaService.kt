package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.solicitud.AsignarRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.CambiarEstadoSolicitudRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.ReasignarDocenteRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.RegistroSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.dto.solicitud.UpdateSolicitudConsultaRequest

interface SolicitudConsultaService {

    fun registrar(request: RegistroSolicitudConsultaRequest): SolicitudConsultaResponse

    fun obtenerPorId(id: Long): SolicitudConsultaResponse

    fun listarTodas(): List<SolicitudConsultaResponse>

    fun listarMisSolicitudes(): List<SolicitudConsultaResponse>

    fun actualizar(id: Long, request: UpdateSolicitudConsultaRequest): SolicitudConsultaResponse

    fun reasignarDocente(id: Long, request: ReasignarDocenteRequest): SolicitudConsultaResponse

    fun asignarRecursoFisico(id: Long, request: AsignarRecursoFisicoRequest): SolicitudConsultaResponse

    fun cambiarEstado(id: Long, request: CambiarEstadoSolicitudRequest): SolicitudConsultaResponse
}