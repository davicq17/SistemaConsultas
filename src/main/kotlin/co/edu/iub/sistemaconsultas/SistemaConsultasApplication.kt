package co.edu.iub.sistemaconsultas

import co.edu.iub.sistemaconsultas.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class SistemaConsultasApplication

fun main(args: Array<String>) {
    runApplication<SistemaConsultasApplication>(*args)
}
