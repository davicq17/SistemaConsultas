package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.repository.ModuloRepository
import co.edu.iub.sistemaconsultas.service.ModuloService
import org.springframework.stereotype.Service

@Service
class ModuloServiceImpl (
    private val moduloRepository: ModuloRepository
): ModuloService {
}