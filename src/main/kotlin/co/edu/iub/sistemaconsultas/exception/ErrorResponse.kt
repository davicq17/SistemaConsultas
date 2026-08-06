package co.edu.iub.sistemaconsultas.exception

import java.time.LocalDateTime

data class ErrorResponse(

    val timestamp: LocalDateTime,

    val status: Int,

    val error: String,

    val message: String,

    val errors: Map<String, String>? = null

)
