package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.bloque.BloqueResponse
import co.edu.iub.sistemaconsultas.dto.bloque.RegistroBloqueRequest
import co.edu.iub.sistemaconsultas.dto.bloque.UpdateBloqueRequest

interface BloqueService {

    fun registrarBloque(request: RegistroBloqueRequest): BloqueResponse

    fun listarBloques(): List<BloqueResponse>

    fun obtenerBloquePorId(id: Long): BloqueResponse

    fun actualizarBloque(id:Long, request: UpdateBloqueRequest): BloqueResponse

    fun eliminarBloque(id: Long)
}