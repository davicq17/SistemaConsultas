package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.service.EmailService
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl: EmailService {

    override fun enviarCorreo(
        destinatario: String,
        asunto: String,
        contenido: String)
    {
        println("========== EMAIL ==========")
        println("Para: $destinatario")
        println("Asunto: $asunto")
        println(contenido)
        println("===========================")
    }
}