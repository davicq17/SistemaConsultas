package co.edu.iub.sistemaconsultas.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(

    var secret: String = "",

    var expirationMinutes: Long = 30

)