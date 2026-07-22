package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.ModuloResponse
import co.edu.iub.sistemaconsultas.dto.RegistroModuloRequest
import co.edu.iub.sistemaconsultas.dto.UpdateModuloRequest

interface ModuloService {

    fun registrarModulo(
        request: RegistroModuloRequest
    ): ModuloResponse

    fun listarModulos(): List<ModuloResponse>

    fun obtenerModuloPorId(
        id: Long
    ): ModuloResponse

    fun actualizarModulo(
        id: Long,
        request: UpdateModuloRequest
    ): ModuloResponse

    fun eliminarModulo(
        id: Long
    )

}