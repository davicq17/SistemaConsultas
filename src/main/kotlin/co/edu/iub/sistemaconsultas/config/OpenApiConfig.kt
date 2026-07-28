package co.edu.iub.sistemaconsultas.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {

        return OpenAPI()

            .info(

                Info()

                    .title("Sistema de Gestión de Consultas Académicas API")

                    .version("1.0.0")

                    .description(
                        "API REST desarrollada en Kotlin + Spring Boot para la gestión de consultas académicas."
                    )

                    .contact(

                        Contact()

                            .name("Equipo de Desarrollo")

                            .email("equipo@universidad.edu")

                    )
            )

            .addSecurityItem(
                SecurityRequirement().addList("Bearer Authentication")
            )

            .components(

                Components()

                    .addSecuritySchemes(

                        "Bearer Authentication",

                        SecurityScheme()

                            .name("Bearer Authentication")

                            .type(SecurityScheme.Type.HTTP)

                            .scheme("bearer")

                            .bearerFormat("JWT")
                    )
            )
    }
}