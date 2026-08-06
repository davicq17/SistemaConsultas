package co.edu.iub.sistemaconsultas.service

interface EmailService {

    fun enviarCorreo(
        destinatario: String,
        asunto: String,
        contenido: String
    )
}