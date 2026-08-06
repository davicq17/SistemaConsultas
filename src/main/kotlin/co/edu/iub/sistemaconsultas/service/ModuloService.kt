package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.modulo.ModuloResponse
import co.edu.iub.sistemaconsultas.dto.modulo.RegistroModuloRequest
import co.edu.iub.sistemaconsultas.dto.modulo.UpdateModuloRequest

interface ModuloService {

    fun registrar(
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