package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.sede.RegistroSedeRequest
import co.edu.iub.sistemaconsultas.dto.sede.SedeResponse
import co.edu.iub.sistemaconsultas.dto.sede.UpdateSedeRequest

interface SedeService {

    fun registrarSede(request: RegistroSedeRequest): SedeResponse

    fun listarSedes(): List<SedeResponse>

    fun obtenerSedePorId(id:Long): SedeResponse

    fun actualizarSede(id:Long, request: UpdateSedeRequest): SedeResponse

    fun eliminarSede(id:Long)
}