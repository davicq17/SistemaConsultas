package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.comentario.ComentarioResponse
import co.edu.iub.sistemaconsultas.dto.comentario.RegistroComentarioRequest
import co.edu.iub.sistemaconsultas.dto.comentario.UpdateComentarioRequest

interface ComentarioService {

    fun registrarComentario(request: RegistroComentarioRequest): ComentarioResponse

    fun obtenerComentarioPorId(id: Long): ComentarioResponse

    fun listarComentariosPorSolicitud(solicitudId: Long): List<ComentarioResponse>

    fun actualizarComentario(id: Long, request: UpdateComentarioRequest): ComentarioResponse
}