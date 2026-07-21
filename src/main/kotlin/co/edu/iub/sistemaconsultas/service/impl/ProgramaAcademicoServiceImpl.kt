package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.repository.ProgramaAcademicoRepository
import co.edu.iub.sistemaconsultas.service.ProgramaAcademicoService
import org.springframework.stereotype.Service

@Service
class ProgramaAcademicoServiceImpl (
    private val programaRepository: ProgramaAcademicoRepository
): ProgramaAcademicoService {
}