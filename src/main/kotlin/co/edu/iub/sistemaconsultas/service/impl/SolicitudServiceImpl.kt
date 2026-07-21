package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.service.SolicitudService
import org.springframework.stereotype.Service

@Service
class SolicitudServiceImpl (
    private val solicitudRepository: SolicitudConsultaRepository
): SolicitudService {
}