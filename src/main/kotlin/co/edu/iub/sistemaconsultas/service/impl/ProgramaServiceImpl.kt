package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.repository.ProgramaAcademicoRepository
import co.edu.iub.sistemaconsultas.service.ProgramaService
import org.springframework.stereotype.Service

@Service
class ProgramaServiceImpl (
    private val programaRepository: ProgramaAcademicoRepository
): ProgramaService {
}