package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.solicitud.RegistroSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.dto.solicitud.UpdateSolicitudConsultaRequest

interface SolicitudService {
    fun crear(request: RegistroSolicitudConsultaRequest): SolicitudConsultaResponse
    fun obtenerPorId(id: Long): SolicitudConsultaResponse
    fun listarTodas(): List<SolicitudConsultaResponse>
    fun actualizar(id: Long, request: UpdateSolicitudConsultaRequest): SolicitudConsultaResponse
    fun eliminar(id: Long)
}