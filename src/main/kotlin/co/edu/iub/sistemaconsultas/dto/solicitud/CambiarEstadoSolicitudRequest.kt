package co.edu.iub.sistemaconsultas.dto.solicitud

import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud

data class CambiarEstadoSolicitudRequest(

    val estado: EstadoSolicitud
)
